package controllers

import (
	"github.com/astaxie/beego/logs"
	"log"
)

func init() {
	go handleMessages()
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
				logs.Info("删除一个 ws 连接！")
				// 当发送消息失败,证明连接已经断开,所以从连接池中移除连接
				delete(clients, client)
			}
		}
	}
}
