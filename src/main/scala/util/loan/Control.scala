package util.loan

/**
  * @author ynupc
  *         Created on 2017/05/01
  */
object Control {
  private type TCloseable = {
    def close(): Unit
  }

  def using[A, B](resource: A)(f: A => B)(implicit r: A => TCloseable): Unit = {
    Option(resource) match {
      case Some(res) =>
        try {
          f(res)
        } finally {
          res.close()
        }
      case None =>
        throw new Exception()
    }
  }

  private type TDisposable = {
    def dispose(): Unit
  }
  implicit def d2c(disposable: TDisposable): TCloseable = {
    new {
      def close(): Unit = disposable.dispose()
    }
  }

  private type TReleasable = {
    def release(): Unit
  }
  implicit def r2c(releasable: TReleasable): TCloseable = {
    new {
      def close(): Unit = releasable.release()
    }
  }
}