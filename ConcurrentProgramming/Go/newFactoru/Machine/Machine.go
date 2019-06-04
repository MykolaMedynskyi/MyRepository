package Machine

import (
	"fmt"
	"math/rand"
	"newFactoru/res"
	"newFactoru/taskServer"
	"time"
)



type MultiplicationMachine struct {
	ID int
	State int
	Broken bool
	WhatState chan string
	StateIs chan int
	IsDone chan string
	Calculate chan *taskServer.Task
	Fixing chan string
}

type AddMachine struct {
	ID int
	State int
	Broken bool
	WhatState chan string
	StateIs chan int
	IsDone chan string
	Calculate chan *taskServer.Task
	Fixing chan string
}

func (m *MultiplicationMachine)Compute(task *taskServer.Task, mode string, isTrue bool) {
	if (isTrue == true) {
		task.Result = -1001
	} else {
		task.Result = task.V1 * task.V2
	}
	time.Sleep(time.Millisecond * res.MULTIPLICATION_MACHINES_TIME)
	m.State = 0
	m.IsDone <- "done"
	if (mode == "g"){
		fmt.Println("			Multiplication machine", m.ID, "complete task", task.ID)
	}
}

func (m *AddMachine) Compute(task *taskServer.Task, mode string, isTrue bool) {
	if (isTrue == true) {
		task.Result = -1001
	} else if (task.Operation == "-") {
		task.Result = task.V1 - task.V2
	} else {
		task.Result = task.V1 + task.V2
	}
	time.Sleep(time.Millisecond * res.ADD_MACHINES_TIME)

	m.IsDone <- "done"
	if (mode == "g") {
		fmt.Println("			Add machine", m.ID, "complete task", task.ID)
	}
	m.State = 0
}

func (m *MultiplicationMachine)ServerStart(mode string) {
	for {
		select {
		case <- m.WhatState:
			m.StateIs <- m.State
			if (m.State == 0) {
				m.State = 1
			}
		case task := <-m.Calculate:
			m.Broken = check()
			m.Compute(task, mode, m.Broken)
		case <-m.Fixing :
			m.Broken = false

		}

	}
}

func (m *AddMachine)ServerStart(mode string) {
	for {
		select {
		case <- m.WhatState:
			m.StateIs <- m.State
			if (m.State == 0) {
				m.State = 1
			}
		case task := <-m.Calculate:
			m.Broken = check()
			go m.Compute(task, mode, m.Broken)
		case <-m.Fixing :
			m.Broken = false
		}
	}
}

func check() bool{
	var random = rand.Intn(100)
	if (random <res.CHANCE_OF_BREAKAGE) {
		return true
	}

	return false
}