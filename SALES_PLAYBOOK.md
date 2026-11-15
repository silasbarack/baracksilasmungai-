# Sales Playbook

How enquiries from the portfolio site become paid projects. The owner dashboard (`/owner`) is the system of record. Open it every working day.

## 1. Pipeline stages

| Stage | Means | Exit when | Auto follow-up |
|---|---|---|---|
| **New** | Form submitted, no reply yet | You have replied | 1 day (the lead is flagged once it has gone 24 hours without a reply) |
| **Contacted** | You replied, and you are qualifying or booking a call | A written proposal or quotation has been sent | 3 days |
| **Quoted** | Proposal sent, waiting for a decision | Deposit paid (→ Won) or a clear no / no reply after the final check-in (→ Lost) | 3 days |
| **Won** | Deposit received | — | none |
| **Lost** | Declined or went silent | — | none (record *why*) |

When you change a lead's stage, the dashboard records the date and sets the next follow-up for you. You can change that date at any time. Every lead in an open stage should have a **deal value** (your quoted or estimated KES amount). Without one, the pipeline total understates what you have in play.

## 2. Daily routine (10 minutes)

1. Open **Pipeline → Needs action today**. Clear every item. The list shows leads that have gone 24 hours or more without a reply, overdue follow-ups, and follow-ups due today.
2. For each item, click **Open**, pick the message template for its stage, and send it by **Email** or **WhatsApp**. The template fills in the client's name, the reference and the project. Edit the text before sending if needed.
3. Move the stage, set the next follow-up date, add a private note, then press **Save**.

**Speed-to-lead target:** reply the same business day. The "Median first response" figure on the dashboard tracks this.

## 3. Follow-up cadence for quotes

| Day | Action | Template |
|---|---|---|
| 0 | Send the proposal (valid 14 days; 50% deposit to start) | *Proposal sent* |
| 3 | Check in and offer to adjust scope or a smaller first phase | *Quote follow-up (day 3)* |
| 10 | Final check-in: close the file politely | *Final check-in (day 10)* |
| — | No reply after day 10 → mark **Lost** and choose the reason "No response" | — |

After a win, send *Won: kickoff* straight away. At handover, send *Ask for a review / referral*. Publish real reviews by adding them to `testimonials` in the Content studio.

## 4. Lead sources: tag every link you share

The site remembers how a visitor first arrived (for 90 days). The dashboard credits each enquiry to that source under **Lead sources**. Add tags to every link you post:

```
https://baracksilasmungai.onrender.com/portfolio?utm_source=whatsapp&utm_medium=status&utm_campaign=nov-2026
https://baracksilasmungai.onrender.com/?utm_source=linkedin&utm_medium=post
https://baracksilasmungai.onrender.com/request-a-quote?utm_source=referral&utm_campaign=jane-wanjiku
https://baracksilasmungai.onrender.com/?ref=business-card
```

Keep the naming consistent and lowercase:

- `utm_source`: where the link is posted (whatsapp, linkedin, instagram, facebook, x, tiktok, email, referral, google-business)
- `utm_medium`: the format (status, post, dm, group, signature, profile)
- `utm_campaign`: an optional label for a push or a person who referred the client

Visitors who arrive without a tag are credited to the referring website (for example `google.com`), or to `direct` when there is none.

## 5. Weekly review (Friday, 20 minutes)

From the **Pipeline** tab with **Last 30 days** selected:

- **Funnel:** where is the biggest drop?
  - Many visits but few quote-form opens → strengthen the calls to action on the case studies.
  - Many form opens but few enquiries → simplify the form.
  - Many enquiries but few quotes → qualify faster and book calls.
  - Many quotes but few wins → review pricing and proposal clarity.
- **Lead sources:** put more time into the channel with the highest *won value*, not the one with the most leads.
- **Most viewed case studies:** feature them first on the home page and in posts.
- **Why deals were lost:** if "Price / budget" dominates, offer a phased first version or a smaller package.
- Export the CSV for a monthly record.

## 6. Editing message templates

Templates live in the site content under `salesTemplates` (Dashboard → Content). Each template has the fields `id`, `stage`, `label`, `subject` and `body`. The available placeholders are `{firstName}`, `{ref}`, `{project}`, `{kind}`, `{value}` and `{portfolioUrl}`.

If `salesTemplates` is missing from the saved content, the defaults in `data/content.json` are used. To customise the templates, copy that array into the Content studio and save. The templates are never sent to public visitors.
