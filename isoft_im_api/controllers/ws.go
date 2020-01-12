package controllers

import (
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/logs"
	"github.com/gorilla/websocket"
	"isoft/isoft_im_api/auth"
	"isoft/isoft_im_api/core"
	"isoft/isoft_im_api/models"
	"log"
	"net/http"
	"strconv"
	"strings"
)

type WSController struct {
	beego.Controller
}

// Upgrader 指定用于将 HTTP 连接升级到 WebSocket连接
var upgrader = websocket.Upgrader{
	ReadBufferSize:  1024,
	WriteBufferSize: 65536,
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

var clients = make(map[*websocket.Conn]bool) // connected clients
var broadcast = make(chan *models.Message)   // broadcast channel

func (c *WSController) Get() {
	r := c.Ctx.Request
	w := c.Ctx.ResponseWriter
	// 头部认证
	if !auth.CheckHeader(r) {
		bytes := []byte("ErrUnauthorized!")
		w.Write(bytes)
		return
	}

	// 将初始 GET 请求升级到 websocket
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Fatal(err)
	}
	// 确保在函数返回时关闭连接
	defer ws.Close()

	// 注册我们的新客户
	clients[ws] = true
	logs.Info("建立一个 ws 连接！")

	loopReadMessage(ws)
}

// 循环读取消息,直到断开连接为止
func loopReadMessage(ws *websocket.Conn) {
	// 第一次建立连接广播常见问题提示
	for _, wrapperMsg := range broadcastMsgWrappers(nil) {
		broadcast <- wrapperMsg
	}
	for {
		var msg models.Message
		// 以JSON形式读入新消息并将其映射到消息对象
		err := ws.ReadJSON(&msg)
		if err != nil {
			// 断开连接时读取会返回错误,需要删除连接
			log.Printf("error: %v", err)
			delete(clients, ws)
			logs.Info("删除一个 ws 连接！")
			break
		}
		// 将新收到的消息发送到广播频道
		for _, wrapperMsg := range broadcastMsgWrappers(&msg) {
			broadcast <- wrapperMsg
		}
		logs.Info("广播一条消息！")
	}
}

// 消息包装
// 1、消息不需要处理,直接转发给其它用户
// 2、不识别消息,默认返回常见问题提示
// 3、识别消息,根据消息做出响应
func broadcastMsgWrappers(msg *models.Message) (msgs []*models.Message) {
	if msg == nil {
		return getCommonQuestions()
	} else if strings.HasPrefix(msg.Message, "common_question:") {
		return getCommonQuestionResponse(msg)
	} else if strings.HasPrefix(msg.Message, "translate:") {
		return getTranslateResponse(msg)
	}
	msgs = append(msgs, msg)
	return msgs
}

func getTranslateResponse(msg *models.Message) (msgs []*models.Message) {
	s := strings.TrimPrefix(msg.Message, "translate:")
	var message string
	if ts, err := core.YDTranslate(s); err == nil {
		message = fmt.Sprintf("<span style='color:green;'>%s</span><br/>", ts)
	} else {
		message = fmt.Sprintf("<span style='color:red;'>%s</span><br/>", "Translate error!")
	}
	msg1 := &models.Message{
		Username: "",
		Message:  message,
	}
	msgs = append(msgs, msg)
	msgs = append(msgs, msg1)
	return msgs
}

func getCommonQuestionResponse(msg *models.Message) (msgs []*models.Message) {
	id, _ := strconv.ParseInt(strings.TrimPrefix(msg.Message, "common_question:"), 10, 64)
	question, _ := models.QueryCommonQuestionById(id)
	questions, _ := models.GetAllCommonQuestion()
	msg = &models.Message{
		Username: "",
		Message: fmt.Sprintf("<span style='color:green;'>%s</span><br/>%s",
			question.QuestionAnswer, models.RenderCommonQuestionsToHtml(questions)),
	}
	msgs = append(msgs, msg)
	return msgs
}

func getCommonQuestions() (msgs []*models.Message) {
	questions, _ := models.GetAllCommonQuestion()
	msg := &models.Message{
		Username: "",
		Message:  models.RenderCommonQuestionsToHtml(questions),
	}
	msgs = append(msgs, msg)
	return msgs
}
