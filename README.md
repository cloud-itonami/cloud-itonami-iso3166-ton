# cloud-itonami-iso3166-ton

**TON**: Kingdom of Tonga.

- Public procurement, routed through general public-finance legislation
  -- Public Finance Management Act 2002 s.44, via the Public Procurement
  Regulations 2015 (amended 22 Feb 2016) -- no free-standing Public
  Procurement Act was found
- Ministry of Commerce, Consumer, Trade, Innovation and Labour
  (MCCTIL) business/company registration (Companies Act 1995, Business
  Licences Act 2002, Registration of Business Names Act 2013) **and**
  foreign investment registration (Foreign Investment Act 2020,
  Foreign Investment Regulations 2021) -- MCCTIL's own genuinely dual
  role, on one integrated online registry platform
  (businessregistries.gov.to)
- Ministry of Revenue and Customs Tax Identification Number (TIN)
  registration and Consumption Tax Act 2003 (15% consumption tax)

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as every other `cloud-itonami-iso3166-*` sibling:

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Ministry of
  Finance's Government Procurement Committee/Central Procurement Unit
  (Public Finance Management Act 2002 s.44, via the Public Procurement
  Regulations 2015 -- no free-standing Public Procurement Act); MCCTIL's
  dual role as both Tonga's company/business registrar (Companies Act
  1995, Business Licences Act 2002, Registration of Business Names Act
  2013) and its foreign-investment-registration authority (Foreign
  Investment Act 2020); and the Ministry of Revenue and Customs' Tax
  Identification Number (TIN) registration process and Consumption Tax
  Act 2003. `governor.cljc`'s flagship check independently recomputes
  whether an engagement's own declared business sector and Tongan-
  equity/paid-up-capital facts actually clear the Foreign Investment
  Act 2020's own Reserved/Restricted-List gate (Foreign Investment
  Regulations 2021, Schedules 1-2) -- an itemized, per-activity table
  of heterogeneous ownership-percentage/paid-up-capital conditions,
  genuinely different in shape from sibling jurisdictions' single flat
  thresholds or uniform categorical gates (this catalog does not force
  a different shape onto Tonga's actual mechanism just to appear
  novel) -- see the namespace docstrings for the full research trail
  and honestly-narrowed scope, including facts this iteration could NOT
  verify (e.g. a general Employment/Labour Act, and any Reserve Bank of
  Tonga role in foreign-investment screening).
- `src/statute/facts.cljc` -- general-law catalog: the Companies Act
  1995 (company law) and the Immigration Act (Chapter 62, Act No. 16 of
  1969) (labor-adjacent, cross-referenced by the Foreign Investment Act
  2020's own s.5(3) as governing non-Tongan-subject employment -- a
  general Employment/Labour Act could not be confirmed this session,
  an honestly disclosed gap).

Every citation is curl/WebFetch-verified against an official source
(`gov.to`, `businessregistries.gov.to`, `finance.gov.to`,
`revenue.gov.to`); where the LIVE official site could not be reached
this session (`mctl.gov.to` serves a mismatched TLS certificate for an
unrelated host; `paclii.org` served a Cloudflare bot-detection
challenge; the Attorney General's Office's own legislation portal
(`ago.gov.to/cms`) is AJAX-driven and only returned a default view to a
non-JS fetch), the SAME official document was instead read from a
`web.archive.org` snapshot where one was available -- see
`marketentry.facts` / `statute.facts`'s docstrings for exactly which
facts are live-verified vs. archived-snapshot-verified vs. an
honestly-flagged gap.

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Tonga:

- `src/culture/facts.cljc` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
