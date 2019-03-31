package Worker

import (
	"fmt"
	"Factory/Chans"
	"Factory/President"
	"Factory/res"
	"math/rand"
	"time"
)

type Worker struct {
	ID int
	TimeForTask int
	President *President.President
}

func (w *Worker)Work(mode string) {
	for {
		rand.Seed(time.Now().UnixNano())

		task := <- w.President.ToDO
		if (mode == "g") {
			fmt.Println("	Worker", w.ID, "I am doing task:", task.ID)
		}
		w.President.DeleteTask(task.ID)
		waitTime := (w.TimeForTask - res.WORKER_DELTA_DELTA) + rand.Intn(res.WORKER_DELTA_DELTA*2)
		time.Sleep(time.Millisecond * time.Duration(waitTime))
		value := calculate(task)

		if (mode == "g") {
			fmt.Println("		Worker", w.ID, "Task:", task.ID, "done, the result is:", value)
		}

		var item = new(Chans.ShopItem)
		item.ID = task.ID
		item.Value = value
		w.President.AddItem(*item)
		w.President.Shop <- *item
	}
}

func CreateTime()int {
	rand.Seed(time.Now().UnixNano())
	time := res.WORKER_TIME - res.WORKER_DELTA + (rand.Intn(res.WORKER_DELTA*2))
	return time
}

func calculate(task Chans.Task) int {

	if (task.Operation == '+') {
		return task.V1 + task.V2
	} else if (task.Operation == '-') {
		return task.V1 - task.V2
	} else {
		return task.V1 * task.V2
	}

}