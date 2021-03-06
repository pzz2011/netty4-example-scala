package com.github.kxbmap.netty.example
package factorial

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import java.lang.{Long => JLong}
import java.math.BigInteger

class NumberEncoder extends MessageToByteEncoder[Number] {
  def encode(ctx: ChannelHandlerContext, msg: Number, out: ByteBuf): Unit = {
    // Convert to a BigInt first for easier implementation.
    val v = msg match {
      case b: BigInt     => b
      case i: Integer    => BigInt(i)
      case l: JLong      => BigInt(l)
      case j: BigInteger => BigInt(j)
      case n             => BigInt(n.toString)
    }

    // Convert the number into a byte array.
    val data = v.toByteArray
    val dataLength = data.length

    // Write a message.
    out.writeByte('F')        // magic number
    out.writeInt(dataLength)  // data length
    out.writeBytes(data)      // data
  }
}
