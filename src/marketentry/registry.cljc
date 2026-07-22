(ns marketentry.registry
  "Pure-function market-entry filing-draft + filing-submit record
  construction -- an append-only market-entry book-of-record draft.

  Like every sibling actor's registry, there is no single international
  reference-number standard for a public-procurement market-entry
  filing -- every jurisdiction assigns its own format. This namespace
  does NOT invent one; it builds a jurisdiction-scoped sequence number
  and validates the record's required fields, the same honest,
  non-fabricating discipline `marketentry.facts` uses.

  `engagement-fee-matches-claim?` is an HONEST reapplication of the SAME
  ground-truth-recompute DISCIPLINE sibling actors use (verify a claimed
  monetary total against the entity's own recorded quantity x unit
  fields), reapplied to a market-entry engagement fee line.

  `reserved-activity?` / `restricted-activity-conditions-satisfied?` /
  `foreign-investment-eligible?` / `foreign-investment-violation?` are
  THIS vertical's own new ground-truth recompute, grounding TON's
  flagship governor check (`marketentry.governor`'s
  `foreign-investment-ineligible-violations`): the Foreign Investment
  Act 2020's own s.5 Reserved/Restricted-List gate (Foreign Investment
  Regulations 2021, Schedules 1-2 -- see `marketentry.facts` for the
  full research trail).

  This is a check SHAPE genuinely different from every prior sibling in
  this family this iteration is aware of: not PNG's single flat
  50%-ownership threshold (Investment Promotion Act 1992 s.28), and not
  Palau's uniform two-tier 'has-any-citizen-owner'/'all-owners-citizens'
  categorical gate (Foreign Investment Act, 28 PNCA s.105). Tonga's own
  Restricted List (Schedule 2 of the Foreign Investment Regulations
  2021) itemizes exactly THREE activities, each with its OWN
  heterogeneous numeric conditions -- a minimum Tongan-equity
  percentage, PRESENT for two items and genuinely ABSENT (own text
  states no such condition) for the third, AND a minimum owner's-
  contribution/paid-up-capital amount in cash, which itself varies by
  item ($500,000 / $250,000 / $250,000). The Reserved List (Schedule 1)
  is a SEPARATE, uniform, absolute bar -- 11 named activities reserved
  exclusively for domestic investors, with no certificate or ownership
  composition able to cure it. The check independently recomputes, from
  the engagement's OWN declared business sector and ownership/capital
  facts (`:tongan-equity-pct` / `:paid-up-capital`), whether that
  activity's own gate is actually satisfied -- it does not trust a
  claimed compliance label.

  `reserved-activity?`/`restricted-activity-conditions` also HONESTLY
  preserve the statute's own scope rather than over-fitting it: an
  unlisted business sector returns nil/false (no s.5 gate applies at
  all -- a Foreign Investment Registration Certificate may still be
  independently required under s.12, unrelated to this specific check),
  NOT a fabricated fourth category.

  This namespace is pure data + pure functions -- no I/O, no network
  call to any real Registrar of Foreign Investment or procurement
  system. It builds the RECORD an operator would keep, not the act of
  submitting a filing itself (that is `marketentry.operation`'s
  `:filing/submit`, always human-gated -- see README Actuation)."
  (:require [clojure.string :as str]))

(defn- unsigned-certificate
  "Every certificate this actor produces is UNSIGNED -- signature is
  the market-entry operator's act, not this actor's."
  [kind subject record-id]
  {"@context" ["https://www.w3.org/ns/credentials/v2"]
   "type" ["VerifiableCredential" kind]
   "credentialSubject" {"id" subject "record" record-id}
   "proof" nil
   "issued_by_registry" false
   "status" "draft-unsigned"})

(defn- zero-pad [n w]
  (let [s (str n)]
    (str (apply str (repeat (max 0 (- w (count s))) "0")) s)))

(defn compute-engagement-fee
  "The ground-truth engagement fee for `engagement`'s own `:base-fee`
  and `:monitoring-months` x `:monthly-rate` -- a single flat
  base + months x rate calculation, not a full pricing engine."
  [{:keys [base-fee monthly-rate monitoring-months]}]
  (+ (double base-fee)
     (* (double monthly-rate) (double monitoring-months))))

(defn engagement-fee-matches-claim?
  "Does `engagement`'s own `:claimed-fee` equal the independently
  recomputed `compute-engagement-fee`?"
  [{:keys [claimed-fee] :as engagement}]
  (== (double claimed-fee) (compute-engagement-fee engagement)))

(def reserved-activities
  "Foreign Investment Act 2020 (Act 7 of 2020) s.7(2)/s.8(1), gazetted
  in Schedule 1 of the Foreign Investment Regulations 2021 (own primary
  text, fetched directly from businessregistries.gov.to, the Ministry
  of Commerce, Consumer, Trade, Innovation and Labour's own official
  registry hosting): business activities reserved EXCLUSIVELY for
  domestic investors -- an overseas person may not carry on ANY of
  these at all, with or without a Foreign Investment Registration
  Certificate (s.5(1)(b))."
  #{:bus-transport-excl-accommodation-operators
    :taxi-and-rental-vehicles-excl-accommodation-operators
    :guesthouse-and-youth-hostel-accommodation
    :used-motor-vehicle-retail-sale
    :bread-manufacture
    :traditional-tongan-cultural-activities
    :green-and-mature-coconut-export
    :reef-or-lagoon-and-coastal-pelagic-fishing
    :near-shore-marine-fishing-under-1000m-depth
    :bottom-fishing-under-500m-depth
    :fish-processing-and-preserving})

(def restricted-activities
  "Foreign Investment Act 2020 s.7(3)/s.8(2), gazetted in Schedule 2 of
  the Foreign Investment Regulations 2021 (own primary text). Unlike
  the Reserved List's uniform domestic-investor-only bar, each
  Restricted List item carries its OWN heterogeneous numeric
  conditions: a minimum Tongan-equity percentage (present for two
  items, `nil` -- genuinely absent, per the Regulations' own text --
  for the third) AND a minimum owner's-contribution/paid-up-capital
  amount in cash, which itself varies by item. A genuinely different
  shape from PNG's single flat ownership-percentage threshold and
  Palau's uniform two-tier categorical citizen-ownership gate."
  {:industrial-tuna-and-deep-water-fishing {:min-tongan-equity-pct 0.30 :min-paid-up-capital 500000}
   :food-beverage-tobacco-retail {:min-tongan-equity-pct nil :min-paid-up-capital 250000}
   :squash-pandanus-kava-and-forestry-growing {:min-tongan-equity-pct 0.30 :min-paid-up-capital 250000}})

(defn reserved-activity?
  "Is `business-sector` on the Foreign Investment Regulations 2021's own
  gazetted Reserved List (Schedule 1)? An overseas person may never
  lawfully carry this on, regardless of ownership composition or
  certificate."
  [business-sector]
  (boolean (contains? reserved-activities business-sector)))

(defn restricted-activity-conditions
  "The Restricted List's own itemized conditions for `business-sector`,
  or nil if it is not a restricted activity at all (an unlisted sector
  is simply nil, not a fabricated condition set)."
  [business-sector]
  (get restricted-activities business-sector))

(defn restricted-activity-conditions-satisfied?
  "Does `tongan-equity-pct`/`paid-up-capital` (the engagement's own
  declared ownership/capital ground truth) satisfy `business-sector`'s
  OWN Restricted List conditions? A sector with no equity condition
  (`:min-tongan-equity-pct nil`, per the Regulations' own text for the
  food/beverage/tobacco-retail item) is satisfied on capital alone. A
  sector that is not a restricted activity at all returns nil (this
  check does not apply -- see `foreign-investment-eligible?` for the
  full three-way dispatch)."
  [business-sector tongan-equity-pct paid-up-capital]
  (when-let [{:keys [min-tongan-equity-pct min-paid-up-capital]} (restricted-activity-conditions business-sector)]
    (boolean
     (and (or (nil? min-tongan-equity-pct)
              (and (some? tongan-equity-pct) (>= (double tongan-equity-pct) min-tongan-equity-pct)))
          (some? paid-up-capital)
          (>= (double paid-up-capital) min-paid-up-capital)))))

(defn foreign-investment-eligible?
  "Is an overseas person's declared `business-sector`/ownership/capital
  ground truth actually eligible under the Act's own s.5/Schedule 1/
  Schedule 2 regime? A Reserved-List sector is NEVER eligible for an
  overseas person. A Restricted-List sector is eligible only if its own
  itemized conditions are satisfied. A sector outside both lists has no
  s.5 gate at all (a Foreign Investment Registration Certificate under
  s.12 may still independently be required, unrelated to this specific
  check)."
  [business-sector tongan-equity-pct paid-up-capital]
  (cond
    (reserved-activity? business-sector) false
    (contains? restricted-activities business-sector)
    (boolean (restricted-activity-conditions-satisfied? business-sector tongan-equity-pct paid-up-capital))
    :else true))

(defn foreign-investment-violation?
  "Does `engagement` declare itself an overseas-person foreign
  investment seeking a Foreign Investment Registration Certificate
  (`:seeks-firc? true`) proposing to operate in a declared
  `:business-sector` that the Act's own Reserved/Restricted-List regime
  bars it from -- either an absolute Reserved-List bar (s.5(1)(b)), or
  a Restricted-List item whose own itemized Tongan-equity-percentage/
  paid-up-capital conditions its own declared facts do not satisfy
  (s.5(2))? Entity-scope-gated: an engagement that does not itself seek
  a FIRC is never flagged by this check (the same discipline PNG's FEC
  check and Palau's reserved-sector check use)."
  [{:keys [seeks-firc? business-sector tongan-equity-pct paid-up-capital]}]
  (boolean
   (and seeks-firc?
        (not (foreign-investment-eligible? business-sector tongan-equity-pct paid-up-capital)))))

(defn register-draft
  "Validate + construct the FILING-DRAFT registration DRAFT -- the
  market-entry operator's own act of preparing a portal registration
  package. Pure function -- does not touch any real procurement
  system."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "draft: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "draft: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "draft: sequence must be >= 0" {})))
  (let [draft-number (str (str/upper-case jurisdiction) "-DFT-" (zero-pad sequence 6))
        record {"record_id" draft-number
                "kind" "filing-draft"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "draft_number" draft-number
     "certificate" (unsigned-certificate "FilingDraft" draft-number draft-number)}))

(defn register-submit
  "Validate + construct the FILING-SUBMIT registration DRAFT -- the
  market-entry operator's own act of actually submitting a filing
  (always human-gated upstream)."
  [engagement-id jurisdiction sequence]
  (when-not (and engagement-id (not= engagement-id ""))
    (throw (ex-info "submit: engagement_id required" {})))
  (when-not (and jurisdiction (not= jurisdiction ""))
    (throw (ex-info "submit: jurisdiction required" {})))
  (when (< sequence 0)
    (throw (ex-info "submit: sequence must be >= 0" {})))
  (let [submit-number (str (str/upper-case jurisdiction) "-SUB-" (zero-pad sequence 6))
        record {"record_id" submit-number
                "kind" "filing-submit"
                "engagement_id" engagement-id
                "jurisdiction" jurisdiction
                "immutable" true}]
    {"record" record "submit_number" submit-number
     "certificate" (unsigned-certificate "FilingSubmit" submit-number submit-number)}))

(defn append [history result]
  (conj (vec history) (get result "record")))
