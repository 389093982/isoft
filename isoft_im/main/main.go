package main

import (
	"github.com/gorilla/websocket"
	"isoft/isoft_im/models"
	"log"
	"net/http"
)

var clients = make(map[*websocket.Conn]bool) // connected clients
var broadcast = make(chan models.Message)    // broadcast channel

// Upgrader 指定用于将 HTTP 连接升级到 WebSocket连接
var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool {
		return true
	},
}

func main() {
	// 配置 websocket 路由
	http.HandleFunc("/ws", handleConnections)

	// 开始监听传入的聊天信息
	go handleMessages()

	err := http.ListenAndServe(":8000", nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}

// 一次 websocket 长连接断开后需要进行 Close  操作
func handleConnections(w http.ResponseWriter, r *http.Request) {
	// 将初始 GET 请求升级到 websocket
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Fatal(err)
	}
	// 确保在函数返回时关闭连接
	defer ws.Close()

	// 注册我们的新客户
	clients[ws] = true
	for {
		var msg models.Message
		// 以JSON形式读入新消息并将其映射到消息对象
		err := ws.ReadJSON(&msg)
		if err != nil {
			// 断开连接时读取会返回错误,需要删除连接
			log.Printf("error: %v", err)
			delete(clients, ws)
			break
		}
		// 将新收到的消息发送到广播频道
		broadcast <- msg
	}
}

func handleMessages() {
	for {
		// 从广播频道获取下一条消息
		msg := <-broadcast
		// 发送到当前连接的每个客户端
		for client := range clients {
			err := client.WriteJSON(msg)
			if err != nil {
				log.Printf("error: %v", err)
				client.Close()
				// 当发送消息失败,证明连接已经断开,所以从连接池中移除连接
				delete(clients, client)
			}
		}
	}
}
