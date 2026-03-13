# Keystore Generation - Quick Reference

## Generate Keystore (Do This ONCE)

### All Platforms

```bash
keytool -genkey -v -keystore release.keystore -alias key0 \
  -keyalg RSA -keysize 2048 -validity 10000
```

**Remember:**
- Keystore password: `_______________`
- Key password: `_______________`  
- Alias: `key0`

⚠️ **SAVE THESE PASSWORDS** - You'll need them for GitHub Secrets!

---

## Convert to Base64

### Windows (PowerShell)
```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.keystore")) | Set-Clipboard
```

### Mac
```bash
base64 -i release.keystore | pbcopy
```

### Linux
```bash
base64 release.keystore | xclip -selection clipboard
```

The base64 string is now in your clipboard!

---

## GitHub Secrets to Add

Go to: **Your Repo → Settings → Secrets and variables → Actions → New repository secret**

Add these 4 secrets:

```
Secret 1:
Name:  KEYSTORE_BASE64
Value: [Paste the base64 string from clipboard]

Secret 2:
Name:  KEYSTORE_PASSWORD
Value: [Your keystore password]

Secret 3:
Name:  KEY_ALIAS  
Value: key0

Secret 4:
Name:  KEY_PASSWORD
Value: [Your key password - usually same as keystore password]
```

---

## Test It

```bash
git add .
git commit -m "Test auto-build"
git push origin main
```

Go to **GitHub → Actions tab** and watch it build!

---

## Backup Your Keystore

**CRITICAL:** Save `release.keystore` somewhere safe (Google Drive, USB, etc.)

If you lose it:
- ❌ Can't update your app
- ❌ Have to release as a "new" app
- ❌ Users can't upgrade, must reinstall

**Keep it safe!** 🔐
