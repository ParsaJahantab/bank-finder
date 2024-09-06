# 🏦 Bank Branch Locator using KD-Tree

## 🌟 Overview

This Java console application helps users find the **nearest branch** of a specified bank using a **KD-Tree** 🌳, a data structure optimized for multidimensional search queries, like finding the closest branch by location. Input includes the bank's name, its branches, and the coordinates 📍 of each branch. The KD-Tree efficiently calculates the nearest branch based on user-provided location data.

This project was originally developed in the **Fall of 2021** as part of a **Data Structures course** 📘, focusing on the practical application of advanced tree structures in real-world scenarios.

## ✨ Features
- 🚀 Efficient nearest neighbor search using a KD-Tree.
- 📊 Handles multidimensional data (coordinates of bank branches).
- 💻 Command-line interface for ease of use.

## 🔧 How It Works
1. **Input**: Users provide the name of the bank and the coordinates of its branches 🏢.
2. **Data Structure**: The program builds a KD-Tree using the provided branch coordinates.
3. **Nearest Neighbor Search**: Given a user's location 📍, the program searches the KD-Tree to find the closest branch.

## 📐 Data Structure Used: KD-Tree
A **KD-Tree** (k-dimensional tree) is a binary search tree designed for efficient searches in a k-dimensional space. In this application, it stores the 2D coordinates of the bank branches 🏙️. When you search for the nearest branch, the KD-Tree quickly filters through the branches to locate the closest one.
