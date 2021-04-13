# Vulnerability Analysis

## A5:2017 Broken Acces Control

### Description

Access control enforces policy such that users cannot act outside of their intended permissions. Failures typically lead
to unauthorized information disclosure, modification or destruction of all data, or performing a business function
outside of the limits of the user. Common access control vulnerabilities include:

* Bypassing access control checks by modifying the URL, internal application state, or the HTML page, or simply using a
  custom API attack tool.
* Allowing the primary key to be changed to another users record, permitting viewing or editing someone else's account.
* Elevation of privilege. Acting as a user without being logged in, or acting as an admin when logged in as a user.
* Metadata manipulation, such as replaying or tampering with a JSON Web Token (JWT) access control token or a cookie or
  hidden field manipulated to elevate privileges, or abusing JWT invalidation
* CORS misconfiguration allows unauthorized API access.
* Force browsing to authenticated pages as an unauthenticated user or to privileged pages as a standard user. Accessing
  API with missing access controls for POST, PUT and DELETE.

### Risk assessment

In this project, there aren't any "roles" or authorization levels. If this application would grow to something bigger
than this, users could give themselves unlimited tries and points or see the word before they even have to guess.

### Counter-measures

#### General

Access control is only effective if enforced in trusted server-side code or server-less API, where the attacker cannot
modify the access control check or metadata.

* With the exception of public resources, deny by default.
* Implement access control mechanisms once and re-use them throughout the application, including minimizing CORS usage.
* Model access controls should enforce record ownership, rather than accepting that the user can create, read, update,
  or delete any record.
* Unique application business limit requirements should be enforced by domain models.
* Disable web server directory listing and ensure file metadata
  (e.g. .git) and backup files are not present within web roots.
* Log access control failures, alert admins when appropriate
  (e.g. repeated failures).
* Rate limit API and controller access to minimize the harm from automated attack tooling.

JWT tokens should be invalidated on the server after logout. Developers and QA staff should include functional access
control unit and integration tests.

#### In this project

* Deny every action by default and only "unlock" actions if needed by a "role"
* Implement logging of actions above a certain authorization level
