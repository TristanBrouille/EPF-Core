import base64

with open('epfcore/src/main/resources/static/images/logo_epf2.png', 'rb') as f:
    logo = base64.b64encode(f.read()).decode()
with open('epfcore/src/main/resources/static/images/Signature.png', 'rb') as f:
    signature = base64.b64encode(f.read()).decode()

html = f"""<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Apercu - Lettres de decision</title>
    <style>
        body {{ font-family: Arial, sans-serif; background: #e0e0e0; padding: 30px; }}
        h1 {{ text-align: center; color: #333; font-size: 18px; margin-bottom: 20px; }}
        .tabs {{ display: flex; gap: 10px; justify-content: center; margin-bottom: 20px; }}
        .tab {{ padding: 10px 24px; cursor: pointer; border: none; border-radius: 6px 6px 0 0; font-size: 14px; font-weight: bold; }}
        .tab.admis {{ background: #2e7d32; color: white; }}
        .tab.refuse {{ background: #c62828; color: white; }}
        .tab.inactive {{ background: #aaa; color: white; }}
        .page {{ background: white; width: 794px; min-height: 1123px; margin: 0 auto; padding: 50px; box-shadow: 0 4px 20px rgba(0,0,0,0.3); box-sizing: border-box; }}
        .header-table {{ width: 100%; border-collapse: collapse; margin-bottom: 40px; }}
        .header-table td {{ vertical-align: top; }}
        .address {{ text-align: right; font-size: 11px; line-height: 1.8; color: #333; }}
        .title {{ text-align: center; font-size: 18px; font-weight: bold; margin-bottom: 6px; text-transform: uppercase; letter-spacing: 1px; }}
        .ref {{ text-align: center; font-size: 11px; color: #888; margin-bottom: 40px; }}
        .date-lieu {{ text-align: right; font-size: 12px; margin-bottom: 30px; }}
        .destinataire {{ margin-bottom: 30px; font-size: 13px; line-height: 1.8; }}
        .objet {{ margin-bottom: 25px; font-size: 13px; }}
        .corps {{ font-size: 13px; line-height: 1.8; margin-bottom: 20px; text-align: justify; }}
        .decision-box-admis {{ background: #e8f5e9; border-left: 5px solid #2e7d32; padding: 15px 20px; margin: 25px 0; font-size: 15px; font-weight: bold; color: #2e7d32; }}
        .decision-box-refuse {{ background: #ffebee; border-left: 5px solid #c62828; padding: 15px 20px; margin: 25px 0; font-size: 15px; font-weight: bold; color: #c62828; }}
        .info-table {{ width: 100%; border-collapse: collapse; margin: 20px 0; font-size: 13px; }}
        .info-table td {{ padding: 5px 10px; }}
        .info-table td:first-child {{ font-weight: bold; width: 180px; color: #555; }}
        .signataire {{ text-align: right; margin-top: 60px; font-size: 13px; }}
        .footer {{ margin-top: 60px; border-top: 1px solid #ddd; padding-top: 10px; text-align: center; font-size: 10px; color: #999; }}
        .hidden {{ display: none; }}
    </style>
</head>
<body>
<h1>Apercu des lettres de decision EPF</h1>
<div class="tabs">
    <button class="tab admis" onclick="show(event, 'admis')">&#10003; Lettre ADMIS</button>
    <button class="tab refuse inactive" onclick="show(event, 'refuse')">&#10007; Lettre REFUSE</button>
</div>

<!-- LETTRE ADMIS -->
<div id="admis" class="page">
    <table class="header-table">
        <tr>
            <td><img src="data:image/png;base64,{logo}" alt="Logo EPF" style="height:70px;"/></td>
            <td class="address">
                FONDATION EPF<br/>
                55 AVENUE DU PRESIDENT WILSON<br/>
                94230 CACHAN<br/>
                www.epf.fr
            </td>
        </tr>
    </table>

    <div class="title">Lettre de decision d'admission</div>
    <div class="ref">Ref : ADM-2025-00123</div>

    <div class="date-lieu">Cachan, le 17 juin 2025</div>

    <div class="destinataire">
        <strong>Martin DUPONT</strong>
    </div>

    <div class="objet"><strong>Objet :</strong> Decision d'admission &mdash; Programme Ingenieur Generaliste &mdash; Campus de Cachan</div>

    <div class="corps">
        Madame, Monsieur,<br/><br/>
        Nous avons bien pris connaissance de votre candidature au programme <strong>Ingenieur Generaliste</strong>
        au sein de l'EPF Ecole d'Ingenieur-e-s, campus de <strong>Cachan</strong>.<br/><br/>
        Apres examen de votre dossier et entretien avec notre jury d'admission, nous avons le plaisir de vous
        informer de la decision suivante :
    </div>

    <div class="decision-box-admis">
        &#10003;&nbsp;&nbsp;Votre candidature est ACCEPTEE
    </div>

    <table class="info-table">
        <tr><td>Candidat :</td><td>Martin DUPONT</td></tr>
        <tr><td>Programme :</td><td>Ingenieur Generaliste</td></tr>
        <tr><td>Campus :</td><td>Cachan</td></tr>
    </table>

    <div class="corps">
        Afin de finaliser votre inscription, vous etes invite(e) a vous connecter a votre espace personnel
        sur le portail EPF. Votre compte aura ete mis a jour avec les droits d'acces etudiant, vous
        permettant d'acceder a l'ensemble des services et ressources mis a votre disposition.<br/><br/>
        Nous vous invitons egalement a prendre connaissance des documents de rentree qui vous seront
        communiques prochainement par email et sur votre espace etudiant.<br/><br/>
        Nous vous souhaitons la bienvenue au sein de l'EPF et vous adressons nos cordiales salutations.
    </div>

    <div class="signataire">
        <strong>Emmanuel DUFLOS</strong><br/>
        Directeur general de l'EPF
    </div>

    <div class="footer">
        EPF Ecole d'Ingenieur-e-s &mdash; 55 avenue du President Wilson &mdash; 94230 Cachan &mdash; www.epf.fr
    </div>
</div>

<!-- LETTRE REFUSE -->
<div id="refuse" class="page hidden">
    <table class="header-table">
        <tr>
            <td><img src="data:image/png;base64,{logo}" alt="Logo EPF" style="height:70px;"/></td>
            <td class="address">
                FONDATION EPF<br/>
                55 AVENUE DU PRESIDENT WILSON<br/>
                94230 CACHAN<br/>
                www.epf.fr
            </td>
        </tr>
    </table>

    <div class="title">Lettre de decision d'admission</div>
    <div class="ref">Ref : ADM-2025-00124</div>

    <div class="date-lieu">Cachan, le 17 juin 2025</div>

    <div class="destinataire">
        <strong>Sophie BERNARD</strong>
    </div>

    <div class="objet"><strong>Objet :</strong> Decision d'admission &mdash; Programme Ingenieur Generaliste &mdash; Campus de Lyon</div>

    <div class="corps">
        Madame, Monsieur,<br/><br/>
        Nous avons bien pris connaissance de votre candidature au programme <strong>Ingenieur Generaliste</strong>
        au sein de l'EPF Ecole d'Ingenieur-e-s, campus de <strong>Lyon</strong>.<br/><br/>
        Apres examen attentif de votre dossier et entretien avec notre jury d'admission, nous sommes au
        regret de vous informer de la decision suivante :
    </div>

    <div class="decision-box-refuse">
        &#10007;&nbsp;&nbsp;Votre candidature n'a pas ete retenue
    </div>

    <table class="info-table">
        <tr><td>Candidat :</td><td>Sophie BERNARD</td></tr>
        <tr><td>Programme :</td><td>Ingenieur Generaliste</td></tr>
        <tr><td>Campus :</td><td>Lyon</td></tr>
    </table>

    <div class="corps">
        Nous vous prions d'agreer, Madame, Monsieur, l'expression de nos salutations distinguees.
    </div>

    <div class="signataire">
        <strong>Emmanuel DUFLOS</strong><br/>
        Directeur general de l'EPF
    </div>

    <div class="footer">
        EPF Ecole d'Ingenieur-e-s &mdash; 55 avenue du President Wilson &mdash; 94230 Cachan &mdash; www.epf.fr
    </div>
</div>

<script>
function show(e, id) {{
    document.getElementById('admis').classList.add('hidden');
    document.getElementById('refuse').classList.add('hidden');
    document.getElementById(id).classList.remove('hidden');
    document.querySelectorAll('.tab').forEach(t => t.classList.add('inactive'));
    e.target.classList.remove('inactive');
}}
</script>
</body>
</html>"""

with open('lettre_decision_apercu.html', 'w', encoding='utf-8') as f:
    f.write(html)
print('OK - fichier genere')
