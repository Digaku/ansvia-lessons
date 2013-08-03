namespace go trancam
namespace java gen.thrift.trancam

struct Ping {
       1: i32 timestamp = 0,
}



service TrancamServer {
	oneway void ping(1:Ping ping),
	i32 mul(1:i32 num1, 2:i32 num2)
}

