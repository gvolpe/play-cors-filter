import filters.CORSFilter
import play.api.GlobalSettings
import play.api.mvc.WithFilters

object Global extends WithFilters(CORSFilter()) with GlobalSettings {

}
