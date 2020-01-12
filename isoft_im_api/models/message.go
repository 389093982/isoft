package models

var (
	MESSAGE_TYPE_DEFAULT                  int64 = 0
	MESSAGE_TYPE_COMMON_QUESTION          int64 = 1
	MESSAGE_TYPE_COMMON_QUESTION_RESPONSE int64 = 2
	MESSAGE_TYPE_TRANSLATE                int64 = 3
	MESSAGE_TYPE_TRANSLATE_RESPONSE       int64 = 4
)

// 定义 Message 对象
type Message struct {
	Username    string `json:"username"`
	Message     string `json:"message"`
	MessageType int64  `json:"message_type"`
}
