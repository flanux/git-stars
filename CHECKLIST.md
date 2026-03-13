# Git Stars - Deployment Checklist ✅

## Before First Push

- [ ] Clone this repo
- [ ] Open in Android Studio
- [ ] Let Gradle sync complete
- [ ] Test run on your device locally

## Keystore Setup (One-Time)

- [ ] Generate keystore with `keytool` command
- [ ] Write down keystore password
- [ ] Write down key password  
- [ ] Write down alias (`key0`)
- [ ] Convert keystore to base64
- [ ] Add `KEYSTORE_BASE64` secret to GitHub
- [ ] Add `KEYSTORE_PASSWORD` secret
- [ ] Add `KEY_ALIAS` secret
- [ ] Add `KEY_PASSWORD` secret
- [ ] Backup `release.keystore` file somewhere safe

## First GitHub Build

- [ ] Make a small change (e.g., edit README.md)
- [ ] `git add .`
- [ ] `git commit -m "Initial commit"`
- [ ] `git push origin main`
- [ ] Go to GitHub → Actions tab
- [ ] Watch the build complete (takes ~5 min)
- [ ] Download APK from Artifacts
- [ ] Install on phone
- [ ] Test the app works

## For Each New Version

- [ ] Make your code changes
- [ ] Test locally first
- [ ] Commit and push
- [ ] Wait for Actions to build
- [ ] Download APK
- [ ] Test on device

## Creating Releases

For version releases (e.g., v1.0.0):

- [ ] Update `versionCode` in `app/build.gradle.kts`
- [ ] Update `versionName` in `app/build.gradle.kts`
- [ ] Commit the version bump
- [ ] Create and push tag: `git tag v1.0.0 && git push origin v1.0.0`
- [ ] Check GitHub Releases page for the new release
- [ ] Share the release link!

## Security Reminders

- [ ] Never commit `release.keystore` to Git (already in .gitignore)
- [ ] Never share your keystore passwords
- [ ] Keep a backup of your keystore file
- [ ] Don't share your GitHub Personal Access Token

## Troubleshooting

If build fails:
1. Check Actions tab for error logs
2. Verify all 4 secrets are set correctly
3. Ensure `KEYSTORE_BASE64` has no spaces/newlines
4. Check that passwords match what you set during keystore creation

If app crashes:
1. Check you entered a valid GitHub token in the app
2. Verify token has `user:follow` and `public_repo` scopes
3. Make sure you follow at least one person on GitHub
4. Check Android Studio Logcat for crash logs

---

**Ready to ship? Let's go! 🚀**
