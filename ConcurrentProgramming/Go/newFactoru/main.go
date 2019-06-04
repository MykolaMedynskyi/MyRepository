package main

import (
	"fmt"
	"newFactoru/Service"
	"newFactoru/ServiceWorker"
	"os"
	"newFactoru/Customer"
	"newFactoru/Machine"
	"newFactoru/MachineServer"
	"newFactoru/President"
	"newFactoru/Worker"
	"newFactoru/itemServer"
	"newFactoru/res"
	"newFactoru/taskServer"
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
		"Type '3' to show info about workers\n" +
		"Type '4' to exit\n\n")
}

func do(request string, taskServer taskServer.TaskServer, itemServer itemServer.ItemServer) {
	if (request == "4") {
		os.Exit(0)
	} else if (request == "1") {
		taskServer.PrintTasks <- "print"
	} else if (request == "2") {
		itemServer.PrintItems <- "print"
	} else if (request == "3") {
		itemServer.PrintWorkersInfo <- "print"
	} else {
		fmt.Println("error")
	}
}

func main() {

	mode := getMode()

	taskServ := taskServer.TaskServer{
		ToDO: make(chan taskServer.Task, 10),
		DeleteTaskC: make(chan taskServer.Task, 10),
		GetTask: make(chan taskServer.Task, res.TASK_LIST_LENGTH),
		PrintTasks: make(chan string, 1),
	}

	itemServer := itemServer.ItemServer{
		Shop: make(chan itemServer.Item,1),
		DeleteItemC: make(chan itemServer.Item,1),
		GetItem: make(chan itemServer.Item, res.SHOP_ITEM_COUNT),
		PrintItems: make(chan string,1),
		TaskDone: make(chan int,1),
		PrintWorkersInfo: make(chan string, 1),
	}

	president := President.President{
		Server: &taskServ,
	}


	var AMachines [res.ADD_MACHINES] Machine.AddMachine
	for i := 0; i < len(AMachines); i++{
		AMachines[i] = Machine.AddMachine{
			ID: i,
			State: 0,
			WhatState: make(chan string,10),
			StateIs: make(chan int,10),
			IsDone: make(chan string,10),
			Calculate: make(chan *taskServer.Task, 10),
			Fixing: make(chan string, 10),
		}
	}

	var MMachines [res.MULTIPLICATION_MACHINES] Machine.MultiplicationMachine
	for i := 0; i < len(MMachines); i++{
		MMachines[i] = Machine.MultiplicationMachine{
			ID: i,
			State: 0,
			WhatState: make(chan string,10),
			StateIs: make(chan int,10),
			IsDone: make(chan string,10),
			Calculate: make(chan *taskServer.Task, 10),
			Fixing: make(chan string, 10),
		}
	}

	machineServer := MachineServer.MachineServer{
		WriteTask: make(chan MachineServer.ReadTask, 50),
		MM:        MMachines,
		AM:        AMachines,
		WhatToFix: make(chan Service.WhatToFix, 50),
	}

	service := Service.Service{
		NeedToFix: make(chan Service.WhatToFix, 10),
		GoFix: make(chan Service.WhatToFix, 10),
		Fixed: make(chan Service.WhatToFix, 10),
	}
	var serviceWorkers [res.SERVICE_WORKERS] ServiceWorker.ServiceWorker

	for i:= 0; i< len(serviceWorkers); i++ {
		serviceWorkers[i] = ServiceWorker.ServiceWorker{
			ID: i,
			Service: &service,
			MachineServer: &machineServer,
		}
	}

	var workers [res.WORKERS] Worker.Worker
	for i := 0; i< len(workers); i++  {
		workers[i] = Worker.Worker{
			ID:          i,
			TimeForTask: Worker.CreateTime(),
			TaskServer:  &taskServ,
			ItemServer: &itemServer,
			Done: 0,
			Behavior: Worker.CreateBehavior(),
			MachineServer: &machineServer,
			Service: &service,
		}
	}

	var customers [res.CUSTOMERS] Customer.Customer
	for i := 0; i< len(customers); i++ {
		customers[i] = Customer.Customer{
			ID: i+1,
			ItemServer: &itemServer,
		}
	}

	go taskServ.GoServer(mode)
	go itemServer.GoServer(mode)
	go president.CreateTasks(mode)


	for i, _ := range MMachines {
		go MMachines[i].ServerStart(mode)
	}
	for i, _ := range AMachines {
		go AMachines[i].ServerStart(mode)
	}

	go machineServer.ServerStart(mode)
	for i, _ := range workers {
		go workers[i].Work(mode)
	}
	for i,_ := range customers {
		go customers[i].GoShopping(mode)
	}
	go service.Work(mode)

	for i, _ := range serviceWorkers {
		go serviceWorkers[i].Work(mode)
	}


	if (mode == "g") {
		fmt.Scanln()
	} else {
		for {
			fmt.Println()
			for i,v := range workers {
				fmt.Print("Worker ", i, " is ", v.Behavior, "; ")
			}
			printOptions()
			var sc string
			fmt.Scanln(&sc)
			do(sc, taskServ, itemServer)
		}
	}

}
