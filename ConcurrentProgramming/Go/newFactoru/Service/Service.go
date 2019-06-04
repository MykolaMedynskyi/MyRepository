package Service

import (
	"newFactoru/res"
)

type WhatToFix struct {
	ID int
	TypeE string
}

type Service struct {

	NeedToFix chan WhatToFix
	GoFix chan WhatToFix
	AMAchins [res.ADD_MACHINES]int
	MMAchins [res.MULTIPLICATION_MACHINES]int

	Fixed chan WhatToFix

}

func (s Service)Work(mode string) {
	for{
		select {
		case wtf := <-s.NeedToFix:
			if (wtf.TypeE == "a") {
				if (s.AMAchins[wtf.ID] == 0) {
					s.AMAchins[wtf.ID] = 1
					s.GoFix <- wtf
				}
			} else {
				if (s.MMAchins[wtf.ID] == 0) {
					s.MMAchins[wtf.ID] = 1
					s.GoFix <- wtf
				}
			}
		case fixed := <- s.Fixed:

			if (fixed.TypeE == "a") {
				s.AMAchins[fixed.ID] = 0
			} else {
				s.MMAchins[fixed.ID] = 0
			}

		}
	}
}