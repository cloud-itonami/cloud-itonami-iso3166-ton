(ns statute.facts
  "General-law compliance catalog for the Kingdom of Tonga (TON) --
  extends this repo's existing `marketentry.facts` (public-procurement
  market-entry only, narrow scope) with a second, orthogonal catalog of
  statutes a company operating in this jurisdiction must generally
  track for compliance. Mirrors cloud-itonami-iso3166-jpn/-deu/-bgr/
  -aze/-alb/-arm/-atg/-ben/-btn/-bwa/-caf/-est/-fji/-png/-plw's
  `statute.facts` (ADR-2607141700, cloud-itonami-compliance-fact-
  federation).

  Every entry cites an OFFICIAL government-hosted URL (or, where the
  live official page/domain could not be reached this session, a
  `web.archive.org` snapshot of that SAME official document, disclosed
  as such) that this iteration actually fetched and read this session
  -- never fabricated.

  - Companies/commercial-entity law: the **Companies Act 1995** (Act 14
    of 1995), confirmed directly this session -- own PDF hosted directly
    by `businessregistries.gov.to` (the Ministry of Commerce, Consumer,
    Trade, Innovation and Labour's own online business registry),
    fetched and read directly, own title page: \"COMPANIES ACT 1995 ...
    Act 14 of 1995\". This iteration did NOT independently read the
    Act's own full 432-page primary text beyond its title/commencement
    pages -- an honest limit, not a claim its substantive provisions
    were read end-to-end.
  - Labor -- this iteration could NOT independently confirm a general
    Employment Act or Labour Act for Tonga this session (see
    `marketentry.facts`'s namespace docstring for the full account of
    what was checked and could not be reached: PacLII's own 1988
    Revised Edition consolidated database -- reached only via
    `web.archive.org` because live `paclii.org` returned the same
    Cloudflare bot-detection challenge this family's other catalogs
    record, not bypassed -- has no title beginning 'Employment' or
    'Labour'/'Labor' under its own C/E/I/L/R letter indexes; the
    Attorney General's Office's own live legislation portal
    (`ago.gov.to/cms`) is AJAX/JavaScript-driven and returned only a
    default current-year view or an empty result shell to this
    session's non-JS fetch; ILO NATLEX's Tonga profile page returned a
    Cloudflare bot-detection challenge with no Wayback snapshot
    available for that query-string URL). Instead, this catalog names
    the **Immigration Act (Chapter 62, Act No. 16 of 1969)** -- a
    narrower, genuinely-existing labor-ADJACENT instrument this
    iteration independently confirmed and cross-checked TWICE this
    session from unrelated official/archived sources: (1) the Foreign
    Investment Act 2020's own text (own primary text, fetched directly
    from `businessregistries.gov.to`), s.5(3): \"Nothing in subsection
    (1) prevents a person carrying on a reserved activity from employing
    a non-Tongan subject whose employment is not prohibited under the
    Immigration Act (Cap. 62) or any other law.\" -- (2) a
    `web.archive.org` snapshot of PacLII's own hosted primary text of
    the Act itself (live `paclii.org` returned the same Cloudflare
    challenge as elsewhere this session, not bypassed): \"LAWS OF TONGA
    [1988 Revised Edition] CHAPTER 62 IMMIGRATION ACT ... Act No. 16 of
    1969 ... AN ACT TO MAKE BETTER PROVISION FOR THE CONTROL OF
    IMMIGRATION.\" This is honestly an entry/immigration-control statute
    (control of entry, permits to reside, removal of unlawful
    immigrants) -- its relevance to labor is specifically the Foreign
    Investment Act 2020's own cross-reference on whether employing a
    non-Tongan subject is prohibited, NOT a general labour-standards,
    minimum-wage, or employment-conditions Act. The broader gap (no
    confirmed general Employment/Labour Act) is disclosed here rather
    than papered over with an invented title.

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit. TON's catalog has 2 entries
  -- company law and a labor-adjacent immigration/non-citizen-employment
  law, both independently confirmed this iteration from official (or,
  for the Immigration Act, Wayback-archived official) Tongan sources
  actually fetched and read this session."
  {"TON"
   [{:statute/id "ton.companies-act-1995"
     :statute/title "Companies Act 1995"
     :statute/jurisdiction "TON"
     :statute/kind :law
     :statute/law-number "Companies Act 1995, Act 14 of 1995, administered by the Ministry of Commerce, Consumer, Trade, Innovation and Labour (MCCTIL) through its online Business Entities Registry (businessregistries.gov.to). Own PDF, hosted directly by MCCTIL's own registry site, fetched and read directly this session; own title page confirms \"COMPANIES ACT 1995 ... Act 14 of 1995\". This iteration did not independently read the Act's own full primary text beyond its title/commencement pages."
     :statute/url "https://www.businessregistries.gov.to/Documentation/TO/CompaniesAct1995_1.pdf"
     :statute/url-provenance :official-mcctil-business-registry
     :statute/enacted-date "1995-01-01"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:corporate-governance :incorporation}}
    {:statute/id "ton.immigration-act-cap-62"
     :statute/title "Immigration Act (Chapter 62)"
     :statute/jurisdiction "TON"
     :statute/kind :law
     :statute/law-number "Immigration Act, Chapter 62 of the Kingdom of Tonga's 1988 Revised Edition of the Laws of Tonga (Act No. 16 of 1969), cross-referenced by the Foreign Investment Act 2020's own s.5(3) (own text, fetched directly from businessregistries.gov.to): \"Nothing in subsection (1) prevents a person carrying on a reserved activity from employing a non-Tongan subject whose employment is not prohibited under the Immigration Act (Cap. 62) or any other law.\" Independently confirmed via a web.archive.org snapshot of PacLII's own hosted primary text (live paclii.org returned a Cloudflare bot-detection challenge this session, not bypassed): \"LAWS OF TONGA [1988 Revised Edition] CHAPTER 62 IMMIGRATION ACT ... Act No. 16 of 1969 ... AN ACT TO MAKE BETTER PROVISION FOR THE CONTROL OF IMMIGRATION.\" An entry/immigration-control statute, not a general labour-standards Act -- see namespace docstring for the honestly-disclosed gap (no general Employment/Labour Act could be confirmed this session)."
     :statute/url "https://web.archive.org/web/20250128130117/http://www.paclii.org/to/legis/consol_act/ia138/"
     :statute/url-provenance :archived-paclii-org
     :statute/enacted-date "1969-01-01"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor :immigration}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-ton statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "TON")) " TON statute(s) seeded with an "
                 "official citation. Extend `statute.facts/catalog`, never "
                 "fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :data-protection)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
