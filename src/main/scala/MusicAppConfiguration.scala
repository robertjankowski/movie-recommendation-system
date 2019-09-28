import MusicAppConfiguration.MusicAppConfigurationException
import com.typesafe.config.Config

trait MusicAppConfiguration {
  def port: Int

  def host: String
}

object MusicAppConfiguration {
  final case class MusicAppConfigurationException(message: String) extends Exception
}

class MusicAppConfigurationImpl(config: Config) extends MusicAppConfiguration {
  override def port: Int = Option(config.getInt("port"))
    .getOrElse(throw MusicAppConfigurationException("Unable to load port"))

  override def host: String = Option(config.getString("host"))
    .getOrElse(throw MusicAppConfigurationException("Unable to load host name"))
}