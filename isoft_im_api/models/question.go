package models

import (
	"fmt"
	"github.com/astaxie/beego/orm"
	"time"
)

type CommonQuestion struct {
	Id              int64     `json:"id"`
	QuestionName    string    `json:"question_name"`
	QuestionAnswer  string    `json:"question_answer" orm:"type(text)"`
	CreatedBy       string    `json:"created_by"`
	CreatedTime     time.Time `json:"created_time" orm:"auto_now_add;type(datetime)"`
	LastUpdatedBy   string    `json:"last_updated_by"`
	LastUpdatedTime time.Time `json:"last_updated_time"`
}

func RenderCommonQuestionsToHtml(questions []CommonQuestion) string {
	msg := "智能客服<span style='color:red;'>小 Y</span> 为您解答,请问您需要咨询什么问题？<br/>"
	for index, question := range questions {
		msg += fmt.Sprintf("<a class='common_question' data='common_question:%d'>%d: %s</a><br/>", question.Id, index+1, question.QuestionName)
	}
	return msg
}

func QueryCommonQuestionById(id int64) (cq CommonQuestion, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("common_question")
	err = qs.Filter("id", id).One(&cq)
	return
}

func GetAllCommonQuestion() (questions []CommonQuestion, err error) {
	o := orm.NewOrm()
	qs := o.QueryTable("common_question")
	_, err = qs.All(&questions)
	return
}
