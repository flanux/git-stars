# Git Stars - Complete Setup Guide

## 🚀 Quick Start (For Absolute Beginners)

### What You Need

1. **Android Studio** - Download from https://developer.android.com/studio
2. **Git** - Download from https://git-scm.com/downloads
3. **GitHub Account** - Sign up at https://github.com

### Step-by-Step Setup

#### Part 1: Get the Code

1. **Open Terminal/Command Prompt**

2. **Clone the repository:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/GitStars.git
   cd GitStars
   ```

3. **Open in Android Studio:**
   - Launch Android Studio
   - Click "Open" 
   - Navigate to the `GitStars` folder
   - Click "OK"

4. **Wait for Gradle Sync:**
   - Android Studio will automatically sync
   - This downloads all dependencies (takes 5-10 minutes first time)
   - You'll see "Gradle build finished" when done

#### Part 2: Run Locally (Test Build)

1. **Connect your Android phone via USB:**
   - Enable "Developer Options" on your phone:
     - Settings → About Phone → Tap "Build Number" 7 times
   - Enable "USB Debugging":
     - Settings → Developer Options → USB Debugging ON
   - Connect phone to computer
   - Allow USB debugging when prompted

2. **Run the app:**
   - In Android Studio, click the green ▶️ (Run) button
   - Select your device
   - App will install and launch

3. **Get GitHub Token:**
   - Open https://github.com/settings/tokens
   - Click "Generate new token (classic)"
   - Give it a name like "GitStars App"
   - Check these boxes:
     - ✅ `user:follow`
     - ✅ `public_repo`
   - Click "Generate token"
   - **COPY THE TOKEN** (you can't see it again!)

4. **Use the app:**
   - Paste your token in the app
   - Click "Save Token"
   - Click "Load Stars"

---

## 🔐 Part 3: GitHub Actions Auto-Build Setup

This is the **magic part** - every time you push code, GitHub automatically builds a signed APK.

### Step 3A: Generate Keystore (One-Time)

**Why?** Android requires apps to be "signed" with a keystore. This proves you're the real developer.

**On Windows:**

1. Open Command Prompt as Administrator

2. Run this command (all one line):
   ```cmd
   keytool -genkey -v -keystore release.keystore -alias key0 -keyalg RSA -keysize 2048 -validity 10000
   ```

3. You'll be asked questions:
   ```
   Enter keystore password: [type a password - REMEMBER THIS!]
   Re-enter new password: [type it again]
   What is your first and last name? [Your Name]
   What is the name of your organizational unit? [Can type "Personal"]
   What is the name of your organization? [Can type "Personal"]
   What is the name of your City or Locality? [Your city]
   What is the name of your State or Province? [Your state]
   What is the two-letter country code? [US, IN, etc.]
   Is CN=..., correct? [yes]
   Enter key password for <key0>: [Can press ENTER to use same password]
   ```

4. A file `release.keystore` will be created in your current folder

**On Mac/Linux:**

Same command but open Terminal instead of Command Prompt.

### Step 3B: Convert Keystore to Base64

**Why?** GitHub can't use the raw keystore file, so we encode it as text.

**On Windows (PowerShell):**

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.keystore")) | Set-Clipboard
```

This copies the base64 string to your clipboard.

**On Mac:**

```bash
base64 -i release.keystore | pbcopy
```

**On Linux:**

```bash
base64 release.keystore | xclip -selection clipboard
```

### Step 3C: Add Secrets to GitHub

1. **Go to your GitHub repo** (the one you pushed)

2. Click **Settings** (top menu)

3. Click **Secrets and variables** → **Actions** (left sidebar)

4. Click **New repository secret**

5. Add these **4 secrets** one by one:

| Name | Value | Where to Get It |
|------|-------|----------------|
| `KEYSTORE_BASE64` | Paste from clipboard (Step 3B) | The long base64 string |
| `KEYSTORE_PASSWORD` | Your password | From Step 3A when you created keystore |
| `KEY_ALIAS` | `key0` | The alias you used in Step 3A |
| `KEY_PASSWORD` | Same as keystore password | Unless you set a different one |

**Example:**

```
Name:  KEYSTORE_BASE64
Value: MIIJRAIBAzCCCPwGCSqGSIb3DQEHAaCCCO0Eggjp... (very long)
```

6. Click **Add secret** for each one

### Step 3D: Test the Workflow

1. **Make any small change** to trigger the build:
   ```bash
   # In your GitStars folder
   git add .
   git commit -m "Test auto-build"
   git push origin main
   ```

2. **Go to GitHub → Actions tab**

3. You'll see a workflow running (yellow dot 🟡)

4. Wait ~5 minutes for it to complete (green check ✅)

5. Click the workflow run → Scroll down to **Artifacts**

6. Download **GitStars-APK**

7. Unzip and install the APK on your phone!

---

## 📱 Installing the APK on Your Phone

### Method 1: Direct Download

1. Upload the APK to Google Drive or Dropbox
2. Open the link on your phone
3. Download the APK
4. You'll see "Can't install unknown apps"
5. Click Settings → Allow from this source
6. Go back and click Install

### Method 2: USB Transfer

1. Connect phone to computer via USB
2. Copy `GitStars-vXXX.apk` to your phone's Downloads folder
3. On phone: Files app → Downloads → Tap the APK
4. Click Install

---

## 🎉 Creating a GitHub Release

Want a proper release with a version number? Do this:

```bash
git tag v1.0.0
git push origin v1.0.0
```

This will:
- Trigger the build
- Create a GitHub Release at `https://github.com/YOUR_USERNAME/GitStars/releases`
- Attach the APK
- Auto-generate release notes from your commits

Users can download directly from the Releases page!

---

## 🐛 Troubleshooting

### "Keystore not found" error in Actions

- Double-check you added all 4 secrets correctly
- Make sure `KEYSTORE_BASE64` has no spaces or newlines

### App crashes on launch

- Check if you entered a valid GitHub token
- Token needs `user:follow` and `public_repo` scopes

### "No repos found"

- Make sure you actually follow people on GitHub
- Those people need to have starred repos (public ones)

### Build fails in GitHub Actions

- Check the **Actions** tab for error logs
- Most common: Wrong Java version (needs JDK 17)
- The workflow should handle this automatically

---

## 📝 Next Steps

Now that you have auto-build working:

1. **Make improvements:**
   - Change the app colors in `ui/theme/Color.kt`
   - Add more features
   - Fix bugs

2. **Push changes:**
   ```bash
   git add .
   git commit -m "Added dark mode toggle"
   git push
   ```

3. **Wait for build** (GitHub Actions does it automatically)

4. **Download new APK** from Actions → Artifacts

5. **Install on phone**

You never need to build on your computer again - GitHub does it for you! 🎉

---

## 🤔 FAQ

**Q: Do I need to rebuild the keystore every time?**  
A: No! Once you set it up, it's forever. Keep the `release.keystore` file safe - if you lose it, you can't update the app.

**Q: Can I use this for Play Store?**  
A: Yes! The signed APK works for Play Store submission. Just use the same keystore.

**Q: What if I lose my keystore?**  
A: You'll have to generate a new one, but users will see it as a different app (can't update).

**Q: How much does this cost?**  
A: $0. GitHub Actions is free for public repos (2,000 minutes/month for private repos).

**Q: Can I build multiple apps this way?**  
A: Yes! Each app gets its own keystore and secrets.

---

Built with 💪 for developers who hate manual builds.
