# App Specifications

![](field%20guide%20mockup.jpg)

### Frontend:

- #### Splash Page: Activity
    - Displays a section to search by name and a section for the SEEK code.
    - Search button that takes you to the search display page

- #### Search Display: (Goal) Activity
    - Nav Bar:
        - Back arrow to return to front page
    - Displays SEEK code or name user entered 
        - They can be separate pages or use the same one with different buttons.
    - For searching by SEEK Code:
        - List all elephant individuals in order of most likely using the SEEK code score calculation. (See SEEK search backend)
        - Elephant listings are clickable and take you to the individual view of the elephant

- #### Individual View:(Goal) Activity
  - Navbar
    - Back arrow to return to the search display page
    - Display [ID] - [NAME]
  - SEEK Code
  - Representative Image
  - Most Recent Sighting: date and coordinates
  - Map displaying coordinates
    - current location of the ranger
    - elephant location history
    - [Check out OSM and osmdroid](https://github.com/osmdroid/osmdroid)
  - Informational gaps identifier (Stretch)
    - Highlight missing information in SEEK code in red.
    - Note from Peter: There are groupings already defined that are separated by "non-attribute" characters. See (absolute) page 69 of this: https://pachydermjournal.org/index.php/pachyderm/article/view/65/317. Also, I've modified the original SEEK format so the age attribute no longer includes the zero.


### Backend:

- #### SEEK search backend (Goal)
  - Need some sort of “closeness” indicator
    -  Use calculation from Website
    -  Hamming Distance
       -  If SEEK is equal, they match
       -  If SEEK elements differ, decrease score
    -  Binary exclusion
       -  Bulls are never cows
  -  Get seekcodes in a list, for each compare to entered code and compute score, display elephant objects sorted by score.

- #### Offline Database (Required) [Room](https://developer.android.com/training/data-storage/room)? 
  - Stores the following
    - SEEK (String, each attribute is a single character)
    - Image
    - Location (history?)
      - Each sighting has a long and lat
    - Last seen
    - Name (Unique)
    - ID (sequential positive integer)
  - Search by name can be a SELECT/WHERE/LIKE clause, easy
  - Search by SEEK needs to be a stored procedure (see SEEK search backend)

- #### ElephantBook API fetcher (Required)
  - Pull-mechanism to grab info from API to update Database
  - Sounds like we don’t require auth, just the API key for upstream API
  - Peters Comment: I'll likely provide an auth key to access the REST API of EB. Bonus if the key can be swapped to access a second EB instance, but a lower development priority.
  - Spec calls for
    - the app will “sync” relevant tables from the database with a parent-to-child, read-only transaction
    - Do this using an API (format TBD)
      - REST API
- #### Map backend (Stretch)
  - No idea how this works or what needs doing, but this is more than just a display(x) kinda deal
  - If we’re displaying this map offline, we need a way to cache large portions of map data 
  - Check out OSM and osmdroid: https://github.com/osmdroid/osmdroid 
































