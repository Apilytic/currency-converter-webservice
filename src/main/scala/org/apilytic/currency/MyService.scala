package org.apilytic.currency

import akka.actor.Actor
import org.apilytic.currency.api.DirectDealExchangeApi
import org.apilytic.currency.api.model.CurrencyRate
import spray.http.MediaTypes._
import spray.http._
import spray.routing._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) {
          // XML is marshalled to `text/xml` by default, so we simply override here
          val currencyRate = new CurrencyRate()
          currencyRate.setAmount("12")
          currencyRate.setFromCurrency("USD")
          currencyRate.setToCurrency("EUR")

          val a = new DirectDealExchangeApi()
          complete(helloX(a.exchangeSingleCurrency(currencyRate).getExchange()))
        }
      }
    }

  def helloX(user: String) =
    <html>
      <body>
        <div>Goodbye { user }</div>
      </body>
    </html>
}

object Test {
  def main(a: Array[String]): Unit = {

    val currencyRate = new CurrencyRate()
    currencyRate.setAmount("12")
    currencyRate.setFromCurrency("USD")
    currencyRate.setToCurrency("EUR")

    val a = new DirectDealExchangeApi()
  }
}
