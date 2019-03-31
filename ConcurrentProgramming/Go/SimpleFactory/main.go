package main

import (
	"fmt"
	"Factory/Chans"
	"Factory/Customer"
	"Factory/President"
	"Factory/Worker"
	"Factory/res"
	"os"
)

func getMode() string{
	if (len(os.Args) == 1) {
		return "g"
	}
	if (len(os.Args) > 3) {
		os.Exit(0)
	}
	if (os.Args[1] == "-s") {
		return "s"
	} else if (os.Args[1] == "-g") {
		return "g"
	} else {
		os.Exit(0)
	}
	return ""
}

func printOptions() {
	fmt.Printf("\nType '1' to show To Do List\n" +
		"Type '2' to show items in shop\n" +
		"Type '3' to exit\n\n")
}

func do(request string, president President.President) {
	if (request == "3") {
		os.Exit(0)
	} else if (request == "1") {
		for _,v := range president.Tasks {
			fmt.Println(v)
		}
	} else if (request == "2") {
		for _, v := range president.Items {
			fmt.Println(v)
		}
	} else {
		fmt.Println("error")
	}
}

func main() {
	mode := getMode()

	president := President.President{
		ToDO: make(chan Chans.Task, res.TASK_LIST_LENGTH),
		Shop: make(chan Chans.ShopItem, res.SHOP_ITEM_COUNT),

	}

	var workers [res.WORKERS] Worker.Worker
	for i := 0; i< len(workers); i++  {
		workers[i] = Worker.Worker{
			ID: i+1,
			TimeForTask: Worker.CreateTime(),
			President: &president,
		}
	}

	var customers [res.CUSTOMERS] Customer.Customer
	for i := 0; i< len(customers); i++ {
		customers[i] = Customer.Customer{
			ID: i+1,
			President: &president,
		}
	}

	go president.CreateTasks(mode)
	for i, _ := range workers {
		go workers[i].Work(mode)
	}
	for i,_ := range customers {
		go customers[i].GoShopping(mode)
	}

	if (mode == "g") {
		fmt.Scanln()
	} else {
		for {
			printOptions()
			var sc string
			fmt.Scanln(&sc)
			do(sc, president)
		}
	}

}
