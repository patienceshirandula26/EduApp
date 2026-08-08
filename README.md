# PicQuiz

An educational picture-puzzle game for Android, built with Kotlin and Jetpack Compose.

Players are shown a visual maths puzzle — animals and fruit standing in for unknown
numbers — and choose the value that replaces the question mark. There are 18 puzzles
across three levels of increasing difficulty.

CP3406 Mobile Application Development, Assessment 3.

---

## The core idea

The puzzle images encode their own answers in the filename:

```
level{LL}_pic{NN}_{ANSWER}.{png|jpg}

assets/1/level01_pic01_0.png    →  level 1, puzzle 1, answer 0
assets/2/level02_pic06_63.jpg   →  level 2, puzzle 6, answer 63
```

Rather than hand-writing 18 question objects, `PuzzleParser` reads the answer straight
off each filename and `PuzzleCatalog` scans the assets folder at runtime. Adding a new
image to `assets/` adds a playable quiz with no code change at all.

Wrong answers are generated around the true one by `AnswerOptions`, using near misses
(±1 to ±5, doubling, halving) so a learner has to actually solve the picture rather
than guess by size. The awkward case is an answer of 0, where naive offsets produce
negatives — that case is handled and unit tested.

---

## Screens

| Screen | Purpose |
|---|---|
| **Welcome** | Name entry. Stored locally with DataStore. |
| **Landing** | Personalised hub — progress summary, navigation, word of the day. |
| **Levels** | Three levels plus a random-level option. |
| **Game** | The main activity screen: puzzle image, four answer options, live timer. |
| **Score** | Round result with sound, animated accuracy bar, replay options. |
| **Statistics** | Quizzes played, accuracy, best score, average and fastest times, full history. |
| **Settings** | Sound on/off, volume slider, countdown option, switch player. |

The brief requires four core screens (Landing, Activity, Settings, Statistics);
Welcome, Levels and Score were added to complete the flow.

---

## Architecture

```
com.example.eduapp/
├── model/          Puzzle, PuzzleParser, AnswerOptions, TimeFormat   (pure Kotlin)
├── data/           PuzzleCatalog, repositories, UserPreferences
├── database/       Room — AppDatabase, AppDao, QuizResult
├── network/        Retrofit — WordApi
├── di/             Koin module
├── screen/         seven Compose screens
├── ui/theme/       colour, type, shapes, reusable game components
├── viewmodel/      AppViewModel
└── helper/         SoundPlayer, asset image loading
```

**MVVM.** Screens observe `StateFlow` and Room `Flow`s through `AppViewModel` and never
touch the database or network directly.

**Repository pattern.** `PuzzleRepository`, `ResultRepository` and `WordRepository` are
interfaces with separate implementations, so the ViewModel depends on abstractions
rather than on Room or Retrofit types.

**Dependency injection with Koin.** Everything the app needs is declared once in
`di/AppModule.kt`. This replaced the template's hand-written `AppViewModelFactory`,
which also removed an unchecked-cast warning.

**Room.** A single `quiz_results` table, indexed on username and level. Every statistic
shown in the app is **derived** from that table with SQL aggregates — nothing is stored
twice, so no figure can drift out of sync. Schemas are exported to `app/schemas/` for
version history.

**DataStore.** Replaces SharedPreferences for player name, sound, volume and the
countdown option. Asynchronous and Flow-based, so a settings change repaints the UI
immediately.

**Networking.** Retrofit fetches a word of the day from dictionaryapi.dev. The result is
wrapped in a `WordState` sealed interface with Loading / Success / Error cases, so the
UI shows a spinner, the definition, or a retry button — never a raw exception.

---

## Multiple players

Every database query is filtered by username. **Switch player** in Settings clears the
stored name and returns to the Welcome screen; entering a different name gives that
person their own scores, history and statistics on the same device.

---

## Sound

`SoundPlayer` uses `SoundPool` rather than `MediaPlayer` — the clips are short and need
to fire with no latency. Four sounds: correct, wrong, level complete, and a longer
fanfare reserved for a perfect round, so the celebration keeps its meaning.

Volume is a 0–1 slider in Settings that previews itself on release. All sounds respect
the on/off toggle.

---

## Design

Deep violet with gold and teal accents, on a soft lavender ground. The puzzle artwork
is pastel, so the app around it goes saturated to make the pictures stand out.

Typography is **Baloo 2** — rounded and chunky, suited to a game and to younger readers.
Display styles are ExtraBold and tightly tracked; body text stays relaxed at 15–17sp.

`ChunkyButton` renders a solid base beneath each button so pressing it looks like the
cap is pushed down, giving the tactile feel of a mobile game. Levels carry their own
gradient — green, blue, pink — so the difficulty ramp reads at a glance.

Rotation is handled with `rememberSaveable`. Because puzzles are shuffled, the shuffle
**seed** is saved rather than the list, so the same order is rebuilt after a
configuration change and the player keeps their place and score.

---

## Ethical design

The brief lists persuasive design patterns as an ethical concern, and gamification is
built from exactly those patterns. Several deliberate choices were made:

- **No countdown by default.** Time pressure disadvantages learners who process more
  slowly or who are anxious. The option exists but is opt-in.
- **No streaks, lives or losable progress.** Nothing punishes a player for stopping or
  for getting something wrong.
- **No leaderboard.** Scores are personal, never ranked against other children.
- **Language that blames the puzzle, not the child.** A zero score reads *"These ones
  were tricky"*, and the replay prompt mentions that puzzles shuffle rather than urging
  another attempt.
- **A gentle wrong-answer sound.** A soft click rather than a harsh buzzer, so mistakes
  feel like part of learning.
- **Data stays on the device.** Only a first name is stored, locally, and the app says
  so plainly on the Welcome screen. Nothing is uploaded and no account is required.
- **Accessibility.** Touch targets are 54–68dp, images carry content descriptions,
  decorative emoji are hidden from screen readers with `clearAndSetSemantics`, and all
  colour comes from `MaterialTheme` tokens so contrast is consistent.

---

## Testing

**Unit tests (JVM, 17 tests)** — run with `./gradlew test`

| Class | Covers |
|---|---|
| `PuzzleParserTest` | filename parsing, zero answers, two-digit answers, malformed files |
| `AnswerOptionsTest` | option count, correct answer always present, no duplicates or negatives |
| `TimeFormatTest` | seconds, whole minutes, mixed, negative input |

The model layer is deliberately free of Android imports so it can be tested on the JVM
without an emulator.

**Instrumented tests (device, 8 tests)** — run with `./gradlew connectedDebugAndroidTest`

| Class | Covers |
|---|---|
| `WelcomeScreenTest` | Compose UI — validation, text entry, privacy notice |
| `PuzzleCatalogTest` | reads the real assets folder: 18 puzzles, 3 levels, answers parsed |

Instrumented tests require a running emulator or connected device.

---

## Building

```
git clone https://github.com/patienceshirandula26/EduApp.git
cd EduApp
./gradlew assembleDebug
```

Android Studio, JDK 17, minSdk 26, targetSdk 36.

---

## Known limitations

- The **countdown timer** setting is stored but does not yet affect gameplay. The
  count-up timer is fully working.
- The dictionary API is unauthenticated and occasionally slow; the app degrades to an
  offline message with a retry button.
- Puzzle difficulty follows the lecturer's original folder grouping rather than being
  calculated from the puzzles themselves.

---

## Attribution

Puzzle images supplied in the unit template. Sound effects from Pixabay (free licence,
no attribution required). Baloo 2 by Ek Type, SIL Open Font Licence. Word definitions
from dictionaryapi.dev.
