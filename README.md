# Git Stars ⭐

A mobile app to discover GitHub repositories starred by people you follow.

## Features

- 🌟 View repos starred by people you follow
- 🔍 Filter by programming language
- 📊 See star counts, forks, and descriptions
- 🎨 Material 3 design with dark/light theme
- 🔒 Secure token storage
- 📱 Native Android (Kotlin + Jetpack Compose)

## How It Works

1. Enter your GitHub Personal Access Token
2. App fetches list of people you follow
3. For each person, fetches their recently starred repos
4. Displays all in a clean, filterable feed

## Setup

### Prerequisites

- Android Studio (latest version)
- JDK 17
- GitHub account with Personal Access Token

### Get GitHub Token

1. Go to https://github.com/settings/tokens
2. Click "Generate new token (classic)"
3. Select scopes:
   - `user:follow` (to read who you follow)
   - `public_repo` (to read public repos)
4. Copy the token (you'll need it in the app)

### Local Development

1. Clone the repo:
```bash
git clone https://github.com/YOUR_USERNAME/GitStars.git
cd GitStars
```

2. Open in Android Studio

3. Sync Gradle and build

4. Run on device/emulator

## GitHub Actions Auto-Build with Signing

This project uses GitHub Actions to automatically build and sign APKs on every push.

### One-Time Keystore Setup

**Step 1: Generate a keystore locally** (only once)

```bash
keytool -genkey -v -keystore release.keystore -alias key0 \
  -keyalg RSA -keysize 2048 -validity 10000
```

When prompted:
- Set a **keystore password** (remember this!)
- Set a **key password** (remember this!)
- Fill in the details (name, org, etc.)

**Step 2: Encode keystore to base64**

```bash
# On Linux/Mac
base64 -i release.keystore | pbcopy

# On Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.keystore")) | Set-Clipboard
```

**Step 3: Add GitHub Secrets**

Go to your repo: **Settings → Secrets and variables → Actions → New repository secret**

Add these 4 secrets:

| Secret Name | Value |
|------------|-------|
| `KEYSTORE_BASE64` | Paste the base64 string from Step 2 |
| `KEYSTORE_PASSWORD` | Your keystore password from Step 1 |
| `KEY_ALIAS` | `key0` (or whatever alias you used) |
| `KEY_PASSWORD` | Your key password from Step 1 |

### How Auto-Build Works

Every time you push to `main`:

1. GitHub Actions triggers
2. Decodes your keystore from base64
3. Builds the release APK
4. Signs it with your keystore
5. Uploads as artifact (available for 30 days)

**To download the signed APK:**

1. Go to your repo → **Actions** tab
2. Click the latest workflow run
3. Scroll down to **Artifacts**
4. Download `GitStars-APK`

### Creating a Release

To create a GitHub Release with the APK:

```bash
git tag v1.0.0
git push origin v1.0.0
```

This will:
- Build and sign the APK
- Create a GitHub Release
- Attach the APK to the release
- Auto-generate release notes

## Project Structure

```
GitStars/
├── app/
│   ├── src/main/java/com/flanux/gitstars/
│   │   ├── data/
│   │   │   ├── Models.kt              # Data classes
│   │   │   ├── GitHubApiService.kt    # Retrofit API interface
│   │   │   └── GitHubRepository.kt    # API calls logic
│   │   ├── ui/
│   │   │   ├── MainViewModel.kt       # State management
│   │   │   ├── MainScreen.kt          # Main UI
│   │   │   └── theme/                 # Material 3 theme
│   │   └── MainActivity.kt            # Entry point
│   └── build.gradle.kts               # App dependencies
├── .github/workflows/build.yml        # Auto-build & sign
└── build.gradle.kts                   # Project config
```

## Tech Stack

- **Kotlin** - Modern Android development
- **Jetpack Compose** - Declarative UI
- **Material 3** - Google's design system
- **Retrofit** - REST API client
- **Coil** - Image loading
- **DataStore** - Token storage
- **Coroutines** - Async operations
- **GitHub API** - Data source

## API Rate Limits

GitHub API has rate limits:
- **5,000 requests/hour** (authenticated)
- This app makes ~1 request per person you follow
- If you follow 100 people, that's ~100 requests

The app doesn't cache yet, so be mindful of the "Refresh" button.

## Future Enhancements

- [ ] Room database for offline caching
- [ ] Pull-to-refresh
- [ ] Search within results
- [ ] "New stars this week" notification
- [ ] Share repos to other apps
- [ ] Sort by stars/date/forks
- [ ] Dark/light theme toggle in-app
- [ ] Multiple GitHub accounts

## Why This App?

GitHub's mobile UI makes it hard to discover repos starred by people you follow. You have to:

1. Go to a person's profile
2. Click "Stars"
3. Scroll through
4. Repeat for every person

This app does it all at once in a clean feed.

## License

MIT

## Contributing

PRs welcome! This is a personal project but feel free to fork and improve.

---

Built with ⭐ by someone tired of GitHub's mobile UX
