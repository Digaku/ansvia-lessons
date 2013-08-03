package main

import (
	"os"
	"net"
	"fmt"
	"github.com/apesternikov/thrift4go/lib/go/src/thrift"
	"./gen-go/trancam"
)

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

	defer trans.Close()

	var protocolFactory = thrift.NewTBinaryProtocolFactoryDefault()

	client := trancam.NewTrancamServerClientFactory(trans, protocolFactory)
	if err = trans.Open(); err != nil {
		fmt.Fprint(os.Stderr, "Error opening socket ", host, ":", port, " ", err)
		os.Exit(1)
	}

	client.Ping(&trancam.Ping{2194})
	rv, _ := client.Mul(50, 5)
	fmt.Println("rv: ", rv)
	
}





