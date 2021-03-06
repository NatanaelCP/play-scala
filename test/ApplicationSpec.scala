import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.{JsNumber, JsString, Json}
import play.api.test._
import play.api.test.Helpers._


/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  sequential

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "respond with an OK status after first PUT request" in new WithApplication {
      val json1 = Json.obj(
        "amount" -> JsNumber(50),
        "type_name" -> JsString("cars")
      )
      val req1 = FakeRequest(
        method = "PUT",
        uri = "/transactionservice/transaction/10",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = json1
      )
      val Some(result) = route(req1)
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
    }

    "respond correctly after GET transaction request" in new WithApplication {
      val req2 = route(FakeRequest(GET, "/transactionservice/transaction/10")).get
      val json2 = Json.obj(
        "amount" -> JsNumber(50),
        "type_name" -> JsString("cars")
      )
      contentAsJson(req2) must be equalTo json2
    }

    "respond correctly after GET type request" in new WithApplication {
      val req3 = route(FakeRequest(GET, "/transactionservice/types/cars")).get
      val json3 = Json.toJson(List(10))
      contentAsJson(req3) must be equalTo json3
    }

    "respond with an OK status after second PUT request" in new WithApplication {
      val json4 = Json.obj(
        "amount" -> JsNumber(100),
        "type_name" -> JsString("shopping"),
        "parent_id" -> JsNumber(10)
      )
      val req4 = FakeRequest(
        method = "PUT",
        uri = "/transactionservice/transaction/11",
        headers = FakeHeaders(
          Seq("Content-type" -> Seq("application/json"))
        ),
        body = json4
      )
      val Some(result) = route(req4)
      status(result) must equalTo(OK)
      contentType(result) must beSome("application/json")
      charset(result) must beSome("utf-8")
    }

    "respond correctly after first GET sum request" in new WithApplication {
      val req5 = route(FakeRequest(GET, "/transactionservice/sum/11")).get
      val json5 = Json.toJson(Map("sum" -> 100))
      contentAsJson(req5) must be equalTo json5
    }

    "respond correctly after second GET sum request" in new WithApplication {
      val req6 = route(FakeRequest(GET, "/transactionservice/sum/10")).get
      val json6 = Json.toJson(Map("sum" -> 150))
      contentAsJson(req6) must be equalTo json6
    }
  }
}

