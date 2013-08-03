package main

import (
	"os"
	"net"
	"fmt"
	"github.com/apesternikov/thrift4go/lib/go/src/thrift"
	"../gen-go/trancam"
)


type TrancamHandler struct {

}

func NewTrancamHandler() *TrancamHandler {
	return &TrancamHandler{}
}

func (t *TrancamHandler) Ping(tp *trancam.Ping) (err error) {
	fmt.Println("got ping: ", tp.Timestamp)
	return nil
}

func (t *TrancamHandler) Mul(num1 int32, num2 int32) (rv int32, err error) {
	rv = num1 * num2
	return rv, nil
}


func main() {

	var trans thrift.TTransport

	var host = "127.0.0.1"
	var port = 7366

	addr, err := net.ResolveTCPAddr("tcp", fmt.Sprint(host, ":", port))
	if err != nil {
		fmt.Fprint(os.Stderr, "Error resolving address", err.Error())
		os.Exit(1)
	}
//	trans, err = thrift.NewTNonblockingSocketAddr(addr)
	trans = thrift.NewTSocketAddr(addr)

	trans.Open()

	defer trans.Close()

	framedTransport := thrift.NewTFramedTransport(trans)
	var protocolFactory = thrift.NewTBinaryProtocolFactoryDefault()
	protocol := protocolFactory.GetProtocol(framedTransport)

	processor := trancam.NewTrancamServerProcessor(NewTrancamHandler())

	//go func(){
		for {
			_, err := processor.Process(protocol, protocol)
			if err != nil {
				fmt.Println("error ", err)
				break
			}
		}
	//}()	
}





