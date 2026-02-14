# 🧩 Development Workflow

## Issues
Create an issue following this naming convention:

- **AAU** — As A User  
- **AAD** — As A Developer  
- **AAT** — As A Tester  

Then add the **subject** of the issue.  
> Example: `AAD, I should fix the bug of ...`

You can assign **labels** and/or **milestones** to make your ticket more precise.

Once the issue is created, **create a branch for it** *(initialy based on `develop`)*.

---

## Commits
When committing, include the **issue number** and a short sentence describing the changes.

> Example:  
> `#23: Bug fixed by adjusting ...`

---

## Pull Requests
Before merging your changes, **create a Pull Request** linked to the issue and **assign a reviewer**.

⚠️ **You cannot merge without a review!**

# UML Delopment
Import VCS'extention [PlantUML](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml) from `jebbs`.

### Necessarly to work
- Java : Platform for PlantUML running.
- [Graphviz](https://www.graphviz.org/download/) : PlantUML requires it to calculate positions in diagram.
