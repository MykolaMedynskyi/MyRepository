package itemServer

import (
	"fmt"
	"newFactoru/res"
)

type ItemServer struct{
	Shop chan Item
	DeleteItemC chan Item
	GetItem chan Item
	PrintItems chan string
	Items []Item

	TaskDone chan int
	PrintWorkersInfo chan string
	Workers [res.WORKERS]int
}

type Item struct {
	ID int
	Value int
}


func (server *ItemServer) GoServer(mode string) {
	for {
		select {
		case createdItem := <-server.Shop:
			if (len(server.Items) == res.SHOP_ITEM_COUNT) {
				if (mode == "g") {
					fmt.Println("There is no space for ITEM: id - ", createdItem.ID)
					continue
				}
			}
			server.AddTask(createdItem)
			server.GetItem <- createdItem

		case deleteItem := <-server.DeleteItemC:
			server.DeleteTask(deleteItem.ID)

		case <-server.PrintItems:
			server.PrintAllItems()
		case worker := <-server.TaskDone:
			server.Workers[worker] ++
		case <- server.PrintWorkersInfo:
			server.printInfo()
		}

	}
}

func (server *ItemServer)DeleteTask(id int) {
	for i, v := range server.Items {
		if (v.ID == id) {
			server.Items = append(server.Items[:i], server.Items[i+1:]...)
			continue
		}
	}
}

func (server *ItemServer)AddTask(elem Item) {
	server.Items = append(server.Items, elem)
}

func (server *ItemServer)PrintAllItems() {
	for _,v := range server.Items {
		fmt.Println(v)
	}
}

func (server *ItemServer)printInfo() {
	for i,v := range server.Workers {
		fmt.Println("Worker", i, " done ", v, "tasks")
	}
}