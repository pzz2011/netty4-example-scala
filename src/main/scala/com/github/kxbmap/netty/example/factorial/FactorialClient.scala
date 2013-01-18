package com.github.kxbmap.netty.example
package factorial

import io.netty.bootstrap.Bootstrap
import io.netty.channel.socket.nio.{NioSocketChannel, NioEventLoopGroup}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Promise}

object FactorialClient extends App with Usage {
  val (host, port, count) =
    parseOptions("<host> <port> <count>") {
      case List(h, p, c) => (h, p.toInt, c.toInt.ensuring(_ > 0))
    }

  val b = new Bootstrap()
  try {
    val answer = Promise[BigInt]()

    b.group(new NioEventLoopGroup())
      .channel(classOf[NioSocketChannel])
      .remoteAddress(host, port)
      .handler(new FactorialClientInitializer(count, answer))

    // Make a new connection.
    b.connect().sync()

    // Retrieve the answer.
    val fractional = Await.result(answer.future, Duration.Inf)

    Console.err.println(f"Factorial of $count%,d is: $fractional%,d")
  }
  finally b.shutdown()
}
