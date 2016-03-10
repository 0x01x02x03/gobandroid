package org.ligi.gobandroid_hd.ui.links

class SGFListFragment : LinkListFragment() {

    internal override fun getData(): Array<LinkWithDescription> {
        return arrayOf(

                // source pro games
                LinkWithDescription("http://www.andromeda.com/people/ddyer/age-summer-94/companion.html", "Companion"),
                LinkWithDescription("http://homepages.cwi.nl/~aeb/go/games/games/Judan/", "Judan"),
                LinkWithDescription("http://gogameworld.com/gophp/pg_samplegames.php", "Commented gogameworld sample games"),
                LinkWithDescription("http://sites.google.com/site/byheartgo/", "byheartgo"),
                LinkWithDescription("http://gokifu.com/", "gokifu"),

                // problems
                LinkWithDescription("http://www.usgo.org/problems/index.html", "USGo Problems"),

                // mixed
                LinkWithDescription("http://www.britgo.org/bgj/recent.html", "Britgo recent"))

    }
}
