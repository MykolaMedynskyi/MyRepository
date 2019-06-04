package Customer

import (
	"fmt"
	"math/rand"
	"newFactoru/itemServer"
	"newFactoru/res"
	"time"
)

type Customer struct {
	ID int
	ItemServer *itemServer.ItemServer
}


func (c *Customer) GoShopping(mode string) {
	for {

		rand.Seed(time.Now().UnixNano())
		waitTime := (res.SHOPPING_TIME - res.SHOPPING_TIME_DELTA) + rand.Intn(res.SHOPPING_TIME_DELTA*2)
		time.Sleep(time.Millisecond * time.Duration(waitTime))

		for i := 0; i< rand.Intn(res.MAX_ITEMS_PER_BUY)+1; i++ {
			item := <- c.ItemServer.GetItem

			c.ItemServer.DeleteItemC <- item
			if (mode == "g") {
				fmt.Println("			Customer", c.ID,"bought item:",item.ID,"( value is",item.Value,")")
			}
		}
	}
}
