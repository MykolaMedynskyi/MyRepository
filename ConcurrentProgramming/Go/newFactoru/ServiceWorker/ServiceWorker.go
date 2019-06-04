package ServiceWorker

import (
	"fmt"
	"newFactoru/MachineServer"
	"newFactoru/Service"
	"newFactoru/res"
	"time"
)

type ServiceWorker struct {
	ID int
	Service *Service.Service
	MachineServer *MachineServer.MachineServer
}

func (w *ServiceWorker) Work (mode string) {
	for {
		select {
			case wtf := <- w.Service.GoFix:

				if (mode == "g") {
					fmt.Println("							service worker ", w.ID, " going to fix machine ", wtf.ID)
				}
				time.Sleep(time.Millisecond * res.TIME_TO_FIX)
				w.MachineServer.WhatToFix <- wtf
				time.Sleep(time.Millisecond * 1000)
				w.Service.Fixed <- wtf
				if (mode == "g") {
					fmt.Println("							service worker ", w.ID, " fixed machine ", wtf.ID)
				}

		}
	}
}
