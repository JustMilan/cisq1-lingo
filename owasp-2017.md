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

## A6:2017 Security Misconfiguration

### Description

Security misconfiguration is the most commonly seen issue. This is commonly a result of insecure default configurations,
incomplete or ad hoc configurations, open cloud storage, misconfigured HTTP headers, and verbose error messages
containing sensitive information. Not only must all operating systems, frameworks, libraries, and applications be
securely configured, but they must be patched and upgraded in a timely fashion.

The application might be vulnerable if the application is:

* Missing appropriate security hardening across any part of the application stack, or improperly configured permissions
  on cloud services.
* Unnecessary features are enabled or installed (e.g. unnecessary ports, services, pages, accounts, or privileges).
* Default accounts and their passwords still enabled and unchanged.
* Error handling reveals stack traces or other overly informative error messages to users.
* For upgraded systems, latest security features are disabled or not configured securely.
* The security settings in the application servers, application frameworks (e.g. Struts, Spring, ASP.NET), libraries,
  databases, etc. not set to secure values.
* The server does not send security headers or directives or they are not set to secure values.
* The software is out of date or vulnerable (see A9:2017-Using Components with Known Vulnerabilities). Without a
  concerted, repeatable application security configuration process, systems are at a higher risk.

### Risk assessment

If this project would have misconfigured HTTP headers or verbose error messages, the system it runs on would be at risk.
It would give malicious parties a greater chance at getting acces to things they should not have acces to. Verbose error
messages could give away some secrets.

Misconfigured HTTP headers would make the game unplayable. This on itself could result in verbose error messages with
all its consequences.

### Counter-measures

#### General

Secure installation processes should be implemented, including:

* A repeatable hardening process that makes it fast and easy to deploy another environment that is properly locked down.
  Development, QA, and production environments should all be configured identically, with different credentials used in
  each environment. This process should be automated to minimize the effort required to setup a new secure environment.
* A minimal platform without any unnecessary features, components, documentation, and samples. Remove or do not install
  unused features and frameworks.
* A task to review and update the configurations appropriate to all security notes, updates and patches as part of the
  patch management process (see A9:2017-Using Components with Known Vulnerabilities). In particular, review cloud
  storage permissions (e.g. S3 bucket permissions).
* A segmented application architecture that provides effective, secure separation between components or tenants, with
  segmentation, containerization, or cloud security groups.
* Sending security directives to clients, e.g. Security Headers.
* An automated process to verify the effectiveness of the configurations and settings in all environments.

#### In this project

* Only install and use the nessecary frameworks
* Let the framework do the authentication and authorization
* Implement separation of concerns
* Include authentication and authorization

## A9:2017 Using Components with Known Vulnerabilities

### Description here

Components, such as libraries, frameworks, and other software modules, run with the same privileges as the application.
If a vulnerable component is exploited, such an attack can facilitate serious data loss or server takeover. Applications
and APIs using components with known vulnerabilities may undermine application defenses and enable various attacks and
impacts.

You are likely vulnerable if:

* You do not know the versions of all components you use
  (both client-side and server-side). This includes components you directly use as well as nested dependencies.
* Software is vulnerable, unsupported, or out of date. This includes the OS, web/application server, database management
  system (DBMS), applications, APIs and all components, runtime environments, and libraries.
* If you do not scan for vulnerabilities regularly and subscribe to security bulletins related to the components you
  use.
* You do not fix or upgrade the underlying platform, frameworks, and dependencies in a risk-based, timely fashion. This
  commonly happens in environments when patching is a monthly or quarterly task under change control, which leaves
  organizations open to many days or months of unnecessary exposure to fixed vulnerabilities.
* Software developers do not test the compatibility of updated, upgraded, or patched libraries.
* You do not secure the components' configurations
  (see [A6:2017 Security Misconfiguration](A6:2017 Security Misconfiguration)).

### Risk assessment

The system would be hackable because known vulnerabilities could be exploited on our system.

### Counter-measures

#### General

There should be a patch management process in place to:

* Remove unused dependencies, unnecessary features, components, files, and documentation.
* Continuously inventory the versions of both client-side and server-side components (e.g. frameworks, libraries) and
  their dependencies using tools like versions, DependencyCheck, retire.js, etc. Continuously monitor sources like CVE
  and NVD for vulnerabilities in the components. Use software composition analysis tools to automate the process.
  Subscribe to email alerts for security vulnerabilities related to components you use.
* Only obtain components from official sources over secure links. Prefer signed packages to reduce the chance of
  including a modified, malicious component.
* Monitor for libraries and components that are unmaintained or do not create security patches for older versions. If
  patching is not possible, consider deploying a virtual patch to monitor, detect, or protect against the discovered
  issue. Every organization must ensure that there is an ongoing plan for monitoring, triaging, and applying updates or
  configuration changes for the lifetime of the application or portfolio.

#### In this project

* Use a bot to check the dependencies used and bump when necessary.
* Automate the process of dependency checking.
* Check the validity of dependencies before using them.
