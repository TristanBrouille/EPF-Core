# 🧩 Development Workflow

This document describes the standard workflow to follow for development, from issue creation to merging code.

---

## 🐞 Issues

Create an issue using the following **naming convention**:

* **AAU** — *As A User*
* **AAD** — *As A Developer*
* **AAT** — *As A Tester*

Then add a clear **subject** describing the goal of the issue.

**Example:**

```
AAD, I should fix the bug of ...
```

You may also assign:

* **Labels** (bug, feature, enhancement, etc.)
* **Milestones** (sprint, release version, etc.)

Once the issue is created:

1. Create a **branch linked to the issue**
2. The branch **must be based on `develop`**

---

## 🧱 Commits

Each commit message must include:

* The **issue number**
* A **short, clear description** of the change

**Example:**

```
#23: Fix bug by adjusting sensor threshold
```

---

## 🔀 Pull Requests

Before merging any changes:

1. Create a **Pull Request (PR)**
2. **Link the PR to its issue**
3. **Assign at least one reviewer**

⚠️ **Merging is strictly forbidden without an approved review.**

---

## 📐 UML Development

For UML diagrams, use **PlantUML** with Visual Studio Code.

### Required VS Code Extension

* **PlantUML** by `jebbs`

  * 👉 [https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)

### Required Dependencies

To work properly, PlantUML requires:

* **Java** — Runtime platform used to execute PlantUML
* **Graphviz** — Required to compute diagram layouts

  * 👉 [https://www.graphviz.org/download/](https://www.graphviz.org/download/)

Make sure both are installed and correctly added to your system PATH.

---

✅ Following this workflow ensures clean version control, traceable changes, and high-quality reviews.
