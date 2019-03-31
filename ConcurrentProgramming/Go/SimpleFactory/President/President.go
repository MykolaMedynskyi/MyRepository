package President

import (
	"fmt"
	"Factory/Chans"
	"Factory/res"
	"math/rand"
	"sync"
	"time"
)


type President struct {
	ToDO chan Chans.Task
	Shop chan Chans.ShopItem
	Tasks []Chans.Task
	Items []Chans.ShopItem
	sync.Mutex
}

func (p *President) CreateTasks(mode string) {
	var id = 0
	for {
		rand.Seed(time.Now().UnixNano())
		var task = new(Chans.Task)
		task.ID = id
		task.V1 = rand.Intn(1000)
		task.V2 = rand.Intn(1000)
		task.Operation = intToOperation(rand.Intn(3))

		if (mode == "g") {
			fmt.Println("Created new task " +
				"( id:", task.ID, " values:", task.V1,",", task.V2, "operation:", string(task.Operation), ")")
		}
		p.AddTask(*task)
		p.ToDO <- *task

		waitTime := (res.PRESIDENT_TIME-res.PRESIDENT_DELTA) + rand.Intn(res.PRESIDENT_DELTA*2)
		time.Sleep(time.Millisecond*time.Duration(waitTime))

		id = id +1
	}
}

func (p *President)AddTask(elem Chans.Task) {
	p.Lock()
	p.Tasks = append(p.Tasks, elem)
	p.Unlock()
}

func (p *President)AddItem(elem Chans.ShopItem) {
	p.Lock()
	p.Items = append(p.Items, elem)
	p.Unlock()

}

func (p *President)DeleteTask(id int) {
	p.Lock()
	for i, v := range p.Tasks {
		if (v.ID == id) {
			p.Tasks = append(p.Tasks[:i], p.Tasks[i+1:]...)
			continue
		}
	}
	p.Unlock()
}

func (p *President)DeleteItem(id int) {
	p.Lock()
	defer p.Unlock()
	for i, v := range p.Items {
		if (v.ID == id) {
			p.Items = append(p.Items[:i], p.Items[i+1:]...)
			continue
		}
	}
}

func intToOperation(n int) rune{
	if (n == 1) {
		return '+'
	} else if (n == 2) {
		return '-'
	} else {
		return '*'
	}
}