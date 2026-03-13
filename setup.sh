#!/bin/bash

# Git Stars - Initial Setup Script
# Run this ONCE when you first clone the repo

echo "🌟 Git Stars - Initial Setup"
echo "=============================="
echo ""

# Check if we're in the right directory
if [ ! -f "settings.gradle.kts" ]; then
    echo "❌ Error: Run this script from the GitStars root directory"
    exit 1
fi

echo "📦 Step 1: Generating Gradle wrapper..."
echo ""

# Generate Gradle wrapper if it doesn't exist
if [ ! -f "gradlew" ]; then
    # Use Gradle from Android Studio if available
    if command -v gradle &> /dev/null; then
        gradle wrapper --gradle-version=8.2 --distribution-type=all
        echo "✅ Gradle wrapper generated"
    else
        echo "⚠️  Gradle not found in PATH"
        echo "   Open this project in Android Studio and it will generate the wrapper automatically"
    fi
else
    echo "✅ Gradle wrapper already exists"
fi

echo ""
echo "🔧 Step 2: Making gradlew executable..."
chmod +x gradlew 2>/dev/null && echo "✅ gradlew is now executable" || echo "⚠️  Run: chmod +x gradlew"

echo ""
echo "📝 Step 3: What to do next..."
echo ""
echo "Option A: Using Android Studio (RECOMMENDED)"
echo "  1. Open Android Studio"
echo "  2. Click 'Open' and select this folder"
echo "  3. Wait for Gradle sync to complete"
echo "  4. Click the green ▶️ button to run"
echo ""
echo "Option B: Command line build"
echo "  ./gradlew assembleDebug"
echo ""
echo "📱 For GitHub Actions auto-build setup, read SETUP_GUIDE.md"
echo ""
echo "🎉 Setup complete! Happy coding!"
