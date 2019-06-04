package taskServer

import (
	"fmt"
	"newFactoru/res"
)

type TaskServer struct{
	ToDO chan Task
	DeleteTaskC chan Task
	GetTask chan Task
	PrintTasks chan string
	Tasks []Task
}

type Task struct {
	ID int
	V1 int
	V2 int
	Operation string
	Result int
}

func (server *TaskServer) GoServer(mode string) {
	for {
		select {
		case createdTask := <-server.ToDO:
			if (len(server.Tasks) == res.TASK_LIST_LENGTH) {
				if (mode == "g") {
					fmt.Println("There is no space for TASK: id - ", createdTask.ID)
					continue
				}
			}
			server.AddTask(createdTask)
			server.GetTask <- createdTask

		case deleteTask := <-server.DeleteTaskC:
			server.DeleteTask(deleteTask.ID)

		case <-server.PrintTasks:
			server.PrintAllTasks()
		}
	}
}

func (server *TaskServer)DeleteTask(id int) {
	for i, v := range server.Tasks {
		if (v.ID == id) {
			server.Tasks = append(server.Tasks[:i], server.Tasks[i+1:]...)
			continue
		}
	}
}

func (server *TaskServer)AddTask(elem Task) {
	server.Tasks = append(server.Tasks, elem)
}

func (server *TaskServer)PrintAllTasks() {
	fmt.Println("sure...")
	for _,v := range server.Tasks {
		fmt.Println(v)
	}
}