package Worker

import (
	"fmt"
	"math/rand"
	"newFactoru/MachineServer"
	"newFactoru/Service"
	"newFactoru/itemServer"
	"newFactoru/res"
	"newFactoru/taskServer"
	"time"
)

type Worker struct {
	ID          int
	TimeForTask int
	TaskServer  *taskServer.TaskServer
	ItemServer *itemServer.ItemServer
	Service *Service.Service
	MachineServer *MachineServer.MachineServer
	Done int
	Behavior string


	Complete chan string
}

func (w *Worker)Work(mode string) {
	for {
		rand.Seed(time.Now().UnixNano())

		task := <- w.TaskServer.GetTask
		if (mode == "g") {
			fmt.Println("	Worker", w.ID, " got task:", task.ID)
		}
		w.TaskServer.DeleteTaskC <- task

		time.Sleep(time.Millisecond * res.WORKER_REST_TIME)

		//

		readTask := &MachineServer.ReadTask{
			Task: &task,
			MachineID: getRnum(task.Operation),
			Operation: task.Operation,
			Resp: make(chan string),
		}
		for {
			if (w.Behavior == "patient") {

				for {

					w.MachineServer.WriteTask <- *readTask
					resp := <-readTask.Resp
					if (resp == "Done") {
						break
					}
				}
			} else {
				a := true
				for (a) {

					w.MachineServer.WriteTask <- *readTask
					select {
					case resp := <-readTask.Resp:
						if (resp == "Done") {
							a = false
						}
					case <-time.After(time.Millisecond * res.WORKER_DELTA_DELTA):
						readTask.MachineID = getRnum(task.Operation)
					}

				}
			}
			if (task.Result == -1001) {
				if (mode == "g") {
					fmt.Println("		Worker", w.ID, " : machine", readTask.MachineID," is broken!")
				}

				var wtf Service.WhatToFix
				if (readTask.Operation == "*") {
					wtf = Service.WhatToFix{readTask.MachineID, "m"}
				} else {
					wtf = Service.WhatToFix{readTask.MachineID, "a"}
				}
				w.Service.NeedToFix <- wtf
				time.Sleep(time.Millisecond * res.WORKER_REST_TIME)
				readTask.MachineID = getRnum(task.Operation)
			} else {
				break
			}
		}


		//

		value := task.Result
		w.Done++
		w.ItemServer.TaskDone <- w.ID
		if (mode == "g") {
			fmt.Println("		Worker", w.ID, " - ",w.Behavior, " Task:", task.ID, "done, the result is:", value, ",Tasks done: ", w.Done)
		}

		var item = new(itemServer.Item)
		item.ID = task.ID
		item.Value = value

		w.ItemServer.Shop <- *item
		time.Sleep(time.Millisecond * res.WORKER_REST_TIME)
		}
}

func CreateTime()int {
	rand.Seed(time.Now().UnixNano())
	time := res.WORKER_TIME - res.WORKER_DELTA + (rand.Intn(res.WORKER_DELTA*2))
	return time
}

func CreateBehavior()string {
	random := rand.Intn(2)
	if (random == 0) {
		return "patient"
	}
	return "impatient"
}

func getRnum (op string) int {
	if (op == "*") {
		return rand.Intn(res.MULTIPLICATION_MACHINES)
	} else {
		return rand.Intn(res.ADD_MACHINES)
	}
}