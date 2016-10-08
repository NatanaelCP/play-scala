package models

case class Transact(amount: Double, type_name: String, parent_id: Option[Long])
case class Sum(sum: Double)


/**
  * Transaction data access
  */

object Transact {
  var transactions:Map[Long,Transact] = Map()

  def save(id: Long, transact: Transact) = {
    transactions += (id -> transact)
  }

  def getTransaction(id: Long): Transact = transactions(id)

  def getIds(type_name: String): List[Long] = {
    var ids:List[Long] = List()
    transactions foreach {case(key,value) => if (value.type_name == type_name) ids = key :: ids}
    ids.sorted
  }

  def getSum(id: Long): Sum = {
    val amount:Double = transactions.get(id) match {
      case Some(x:Transact) => {
        var am = x.amount
        transactions foreach {case(key,value) => value.parent_id match {
          case Some(y:Long) => if(id == y && id != key) am += value.amount else am
          case None => am
          }
        }
        am
      }
      case None  => 0
    }
    Sum(amount)
  }
}