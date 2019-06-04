package MachineServer

import (
	"newFactoru/Machine"
	"newFactoru/Service"
	"newFactoru/res"
	"newFactoru/taskServer"
)

type ReadTask struct {
	Task *taskServer.Task
	MachineID int
	Operation string
	Resp chan string
}

type MachineServer struct {

	WriteTask chan ReadTask

	MM [res.MULTIPLICATION_MACHINES] Machine.MultiplicationMachine
	AM [res.ADD_MACHINES] Machine.AddMachine

	ATasks [] taskServer.Task
	MTasks [] taskServer.Task

	WhatToFix chan Service.WhatToFix

}

func (ms *MachineServer)ServerStart(mode string) {
	for {
		select {
			case read := <-ms.WriteTask:
				if (read.Operation == "*") {
					ms.MM[read.MachineID].WhatState <- "g"
					var st= <-ms.MM[read.MachineID].StateIs
					if (st == 0) {
						ms.MM[read.MachineID].Calculate <- read.Task
						<-ms.MM[read.MachineID].IsDone
						read.Resp <- "Done"
					} else {
						read.Resp <- "Nope"
					}
				} else {
					ms.AM[read.MachineID].WhatState <- "g"
					var st= <-ms.AM[read.MachineID].StateIs
					if (st == 0) {
						ms.AM[read.MachineID].Calculate <- read.Task
						<-ms.AM[read.MachineID].IsDone
						read.Resp <- "Done"
					} else {
						read.Resp <- "Nope"
					}
			}
			case fix := <- ms.WhatToFix:

				if (fix.TypeE == "m") {
					ms.MM[fix.ID].Fixing <- "fix"
				} else {
					ms.AM[fix.ID].Fixing <- "fix"
				}

		}
	}
}


