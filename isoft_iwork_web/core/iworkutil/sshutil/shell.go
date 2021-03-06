package sshutil

import (
	"io"
	"time"
)

func RunSSHShellCommandOnly(sshAccount, sshPwd, sshIp, command string, timeout int64) error {
	return RunSSHShellCommand(sshAccount, sshPwd, sshIp, command, nil, nil, timeout)
}

func RunSSHShellCommand(sshAccount, sshPwd, sshIp, command string, stdout, stderr io.Writer, timeout int64) error {
	client, session, err := SSHConnect(sshAccount, sshPwd, sshIp, 22)
	if err != nil {
		return err
	}
	// 避免 err 导致 session 空指针异常,需要放在 err 判断之后
	defer client.Close()
	defer session.Close()

	timeoutFlag := false

	if timeout > 0 {
		// 设置 shell 超时时间
		go func() {
			time.Sleep(time.Duration(timeout) * time.Second)
			client.Close()
			session.Close()
			timeoutFlag = true
		}()
	}

	session.Stdout = stdout
	session.Stderr = stderr
	err = session.Run(command)
	if !timeoutFlag && err != nil {
		return err
	}
	return nil
}
