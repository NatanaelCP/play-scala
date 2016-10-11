package models

case class Transact(amount: Double, type_name: String, parent_id: Option[Long])
case class Sum(sum: Double)


/**
  * Transaction data access
  */

object Transact {
  private var transactions:Map[Long,Transact] = Map()

  def save(id: Long, transact: Transact) = {
    this.synchronized {
      transactions += (id -> transact)
    }
  }

  def getTransaction(id: Long): Transact = transactions(id)

  def getIds(type_name: String): List[Long] = {
    transactions
      .view
      .collect {case (key, value) if(value.type_name == type_name) => key}
      .toList
  }

  def getSum(id: Long): Sum = {
    Sum (
      transactions
        .filter { case (key, value) => value.parent_id match {
          case Some(y:Long) => y == id || key == id
          case None => key == id
          }
        }
        .map {case (_, value) => value.amount}
        .sum
    )
  }
}