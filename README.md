# 2023 ITM-Mobile Programming Project
## SNUST application customized for the exchange student 💯🏅
### [4-Inner] in your pocket: Your campus, your way.

#### Project Name : 4-Inner
#### Project execution period : 2023.10.30~2023.12.18
#### Project Hosting : [Junior LV5] ITM 523 Mobile Programming
#### Project Participants : Namsub Kim [18102071], Jaeyu Lee [18102088], Suho Lee [19102127]
-----------------------
## Description

- <b>Goal</b>
  
: Since there is no app for Seoul National University of Science and Technology exclusively for foreigners, we created an app for their convenience and adaptation to school life.

- <b>Brief Description</b>

: This is an app for foreign students at Seoul National University of Science and Technology that is equipped with an English-only bulletin board, timetable, campus directions, OCR, and AR functions for communication between students on campus.

- <b>Target User</b>
  
: For Seoultech foreign students who are main target user. 
: For Domestic students who want to become friends with foreigners

-----------------------
## function list

|Category|Function|
|------|---|
|<b>Main</b>|Login information, Today’s class, Recent 3 Posts, Recent Destination|
|<b>Board</b>|View Postings, CRUD, Comment upload&delete|
|<b>Timetable</b>|Refelect added lecture information, Delete the lecture that user want|
|<b>Map</b>|Find directions to the desired building based on the user's location|
|<b>A/R</b>|AR-based route finding|

-----------------------
## Technical Methodology

- <b>Database</b>
  
: Firebase Authentication, Firebase Firestore, Room DB

- <b>Widgets/Components</b>

: Textview, Button, ImageView, Spinner, Fragment, ToggleButton, ImageButton, EditText

- <b>Layout</b>

: LinearLayout, ConstraintLayout, FrameLayout

- <b>Container</b>
  
: View, RecyclerView, ScrollView, BottomNavigationView, ListView

- <b>3rd Libraries & Frameworks</b>

: Firebase, Kakao Map API, Glide, Unity, RoomDB

-----------------------
## Module Introduction

|Category|Module|
|------|---|
|<b>activities</b>|LoginActivity|
|<b>activities</b>|MyPageActivity|
|<b>activities</b>|MyPageActivity2|
|<b>activities</b>|NaviActivity|
|<b>activities</b>|OnBoardingActivity|
|<b>activities</b>|SignUpActivity|
|<b>fragments</b>|ArFragment|
|<b>fragments</b>|BulletinFragment|
|<b>fragments</b>|HomeFragment|
|<b>fragments</b>|MapFragment|
|<b>fragments</b>|RecentDestinationFragment|
|<b>fragments</b>|RecentPostFragment|
|<b>fragments</b>|TimetableFragment|
|<b>fragments</b>|TodayClassFragment|
|<b>post</b>|Comment|
|<b>post</b>|Post|
|<b>post</b>|Posting|
|<b>post</b>|RecyclerCommentAdapter|
|<b>post</b>|UpdatePost.kt|
|<b>timetable</b>|TimeTableAdapter|
|<b>timetable</b>|TimeTableDAO|
|<b>timetable</b>|TimeTableDB|
|<b>X</b>|Board.kt|
|<b>X</b>|FireBase|
|<b>X</b>|FragmentTags|
|<b>X</b>|PreferenceHelper|
|<b>X</b>|recent_post.kt|
|<b>X</b>|RecyclerItemAdapter.kt|
|<b>X</b>|TimeTable.kt|
|<b>X</b>|UniversitySites.kt|
|<b>X</b>|UserData.kt|

-----------------------
## Default Configuration


>        applicationId = "com.example.a4_inner"
>        minSdk = 30
>        targetSdk = 33
>        versionCode = 1
>        versionName = "1.0"
>
>        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


-----------------------
## UI (User Interface)

-----------------------
## Dependencies
> *minumum android sdk version: 30*

> *android room:2.4.0*

> *glide:4.12.0*

> *firebase auth:22.2.0*

> *firebase firestore:20.3.0*

> *gms location:21.0.1*

> *kakaomap:2.6.0*
-----------------------
## Dijkstra Algorithm
54 sites in university and 69 paths among them
And bydirectional Dijkstra algorithm is implemented in 'UniversitySites.kt'
