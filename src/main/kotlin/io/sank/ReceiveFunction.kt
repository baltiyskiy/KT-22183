import scala.PartialFunction
import scala.runtime.BoxedUnit

class ReceiveFunction(private val msgProcessor: (Any) -> Unit?) : PartialFunction<Any, BoxedUnit> {
  override fun apply(v1: Any?): BoxedUnit {
    return BoxedUnit.UNIT
  }

  override fun isDefinedAt(x: Any?): Boolean {
    return x != null && msgProcessor(x) != null
  }

  override fun <A1 : Any?, B1 : Any?> applyOrElse(x: A1, default: scala.Function1<A1, B1>): B1 {
    val result = if (x != null) msgProcessor(x) else null
    if (result != null) {
      // In fact, B1 has a lower bound of BoxedUnit, but Kotlin cannot express that, 
      // and in Java it also doesn't have a lower bound ("super") for some reason
      @Suppress("UNCHECKED_CAST")
      return result as B1
    }
    return default.apply(x)
  }
}