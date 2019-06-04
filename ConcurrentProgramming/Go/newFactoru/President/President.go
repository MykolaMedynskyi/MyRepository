package President

import (
	"newFactoru/res"
	"fmt"
	"math/rand"
	"newFactoru/taskServer"
	"time"
)

type President struct {
	Server      *taskServer.TaskServer
}

func (p *President) CreateTasks(mode string) {
	var id = 0
	for {
		rand.Seed(time.Now().UnixNano())
		var task = new(taskServer.Task)
		task.ID = id
		task.V1 = rand.Intn(1000)
		task.V2 = rand.Intn(1000)
		task.Operation = intToOperation(rand.Intn(3))

		if (mode == "g") {
			fmt.Println("Created new task " +
				"( id:", task.ID, " values:", task.V1,",", task.V2, "operation:", string(task.Operation), ")")
		}
		p.Server.ToDO <- *task

		waitTime := (res.PRESIDENT_TIME-res.PRESIDENT_DELTA) + rand.Intn(res.PRESIDENT_DELTA*2)
		time.Sleep(time.Millisecond*time.Duration(waitTime))

		id = id +1
	}
}

func intToOperation(n int) string{
	if (n == 1) {
		return "+"
	} else if (n == 2) {
		return "-"
	} else {
		return "*"
	}
}
