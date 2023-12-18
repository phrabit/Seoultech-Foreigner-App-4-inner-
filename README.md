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
## Dependencies
> *minumum android sdk version: 30*

> *android room:2.4.0*

> *glide:4.12.0*

> *firebase auth:22.2.0*

> *firebase firestore:20.3.0*

> *gms location:21.0.1*

> *kakaomap:2.6.0*
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

| Splash | Login | Onboarding  |
| --- | --- | ---  |
| ![Splash](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/b8bc416b-a59d-4041-bc7f-a89a94cb4235)  | ![Login](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/b33e1183-13fc-45a4-a9b3-95ada18a1a86)  | ![Onboarding](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/d6357225-4229-4522-b898-55df8d3b362c)

| Main Page | My page | Class Info  |
| --- | --- | ---  |
| ![MainPage](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/3334d9f0-2be6-46c1-b0fc-1cb74402fa0c)  | ![MyPage](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/81694d91-90d8-431d-8bf2-2618547813b1)  | ![class Info](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/08dcf698-72ac-42e0-808c-00deb600637a)

| Board | Posting | Comment  |
| --- | --- | ---  |
| ![BulletinBoard](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/d9ec1ba8-5a4a-4c04-8c3d-e559e8c942a3)  | ![Posting](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/52bb2dde-a225-4c7c-972f-077dbf8d6b74)  | ![Comment](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/f1cd2c02-4896-4028-950d-3c2126339b8d)


| Timetable | Map | A/R  |
| --- | --- | ---  |
| ![Timetable](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/96356a9e-b8a6-4172-a8c9-a30516132013)  | ![Map](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/2bbb46ee-f36b-4619-85b9-5bf519a3b01f)  | ![AR](https://github.com/phrabit/Seoultech-Foreigner-App-4-inner-/assets/70180003/10f0110e-8179-4fa3-b669-e562dd92ba15)

-----------------------
## Dijkstra Algorithm
54 sites in university and 69 paths among them
And bydirectional Dijkstra algorithm is implemented in 'UniversitySites.kt'

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
