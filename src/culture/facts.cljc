(ns culture.facts
  "Country-level regional-culture catalog for Tonga (TON) -- national
  dishes, protected products, beverages, crafts, festivals and heritage
  sites, per ADR-2607171400 addendum 2 (cloud-itonami-municipality-
  culture-catalog Wave 1, in com-junkawasaki/root). Sibling namespace to
  `marketentry.facts` / `statute.facts` (ADR-2607141700); city-level
  counterparts live in the cloud-itonami-municipality-* repos.

  Catalog is keyed by UPPERCASE ISO3 (mirrors `statute.facts`); entries
  carry no :culture/municipality (that attribute is city-level only).

  Every entry cites a source URL that was actually fetched and read on
  :culture/retrieved-at -- never fabricated. Summaries state only what the
  cited source confirms. An item not in this table has NO spec-basis, full
  stop; extend `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of culture entries."
  {"TON"
   [{:culture/id "ton.dish.lu-pulu"
     :culture/name "Lū Pulu"
     :culture/country "TON"
     :culture/kind :dish
     :culture/summary "Tongan parcel dish of taro leaves (lu talo) wrapped around corned beef and coconut cream; the Wikipedia Luʻau article's Tonga section describes lu pulu specifically, while noting taro-leaf luʻau-style dishes are found across several Pacific nations."
     :culture/url "https://en.wikipedia.org/wiki/L%C5%AB%CA%BBau_(food)"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ton.dish.topai"
     :culture/name "Topai"
     :culture/country "TON"
     :culture/kind :dish
     :culture/summary "Tongan dish of flour and water worked into a paste and dropped into boiling water, then served with a syrup of sugar and coconut milk, per the Wikipedia article on Tongan cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Tongan_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ton.dish.ma"
     :culture/name "Mā"
     :culture/country "TON"
     :culture/kind :dish
     :culture/summary "Traditional Tongan preserve of breadfruit, banana, and taro stored in pits until fermented into a staple food, per the Wikipedia article on Tongan cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Tongan_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ton.beverage.otai"
     :culture/name "ʻOtai"
     :culture/country "TON"
     :culture/kind :beverage
     :culture/summary "Popular Tongan drink of pulped watermelon mixed with coconut milk, per the Wikipedia article on Tongan cuisine."
     :culture/url "https://en.wikipedia.org/wiki/Tongan_cuisine"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ton.beverage.kava"
     :culture/name "Kava"
     :culture/country "TON"
     :culture/kind :beverage
     :culture/summary "Ceremonial and social drink prepared from the kava plant's root; in Tonga it is drunk nightly at kalapu (\"clubs\") and a formal kava ceremony is a required component of the accession rites for a King of Tonga."
     :culture/url "https://en.wikipedia.org/wiki/Kava_culture"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ton.craft.ngatu"
     :culture/name "Ngatu"
     :culture/country "TON"
     :culture/kind :craft
     :culture/summary "Tongan tapa cloth beaten from paper mulberry bark and painted with carved wooden drums (kupesi); families are considered poor if they lack ngatu to give at weddings, funerals, and other life events."
     :culture/url "https://en.wikipedia.org/wiki/Tapa_cloth"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}
    {:culture/id "ton.heritage.haamonga-a-maui"
     :culture/name "Haʻamonga ʻa Maui"
     :culture/country "TON"
     :culture/kind :heritage
     :culture/summary "Stone trilithon on Tongatapu, built in the 13th century by King Tuʻitātui in honor of his two sons; sometimes called the \"Stonehenge of the Pacific\" and protected as a national park since 1972."
     :culture/url "https://en.wikipedia.org/wiki/Ha%CA%BBamonga_%CA%BBa_Maui"
     :culture/url-provenance :wikipedia-en
     :culture/retrieved-at "2026-07-17"}]})

(defn spec-basis [iso3] (get catalog iso3))

(defn coverage
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ton culture catalog "
                 "(ADR-2607171400 addendum 2, Wave 1): " (count (get catalog "TON"))
                 " TON entries, each with a fetched-and-read citation. "
                 "Extend `culture.facts/catalog`, never fabricate an id/url.")})))

(defn by-kind [iso3 kind]
  (filterv #(= (:culture/kind %) kind) (spec-basis iso3)))
