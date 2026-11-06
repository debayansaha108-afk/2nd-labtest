# Multi-Page Web Services (HTTP Requests) Experiment

The user requested multiple working pages/screens for the Web Services experiment instead of just a single list page.

## User Review Required

> [!NOTE]
> This plan introduces a multi-page Fragment-based navigation structure inside `MainActivity`, providing a Home Dashboard and multiple distinct HTTP request demos (Users API and Products API), plus a Detail View when selecting an item.

## Proposed Changes

### 1. Navigation & Architecture
- **`MainActivity.kt`**: Acts as the single host activity managing Fragment transactions (Home -> Users / Products -> Detail).

### 2. Fragments & Layouts
- **`HomeFragment.kt` & `fragment_home.xml`**: Dashboard menu with buttons to navigate to different web service endpoints.
- **`UsersFragment.kt` & `fragment_users.xml`**: Fetches and displays users from DummyJSON.
- **`ProductsFragment.kt` & `fragment_products.xml`**: Fetches and displays products from DummyJSON (`https://dummyjson.com/products`).
- **`UserDetailFragment.kt` & `fragment_user_detail.xml`**: Shows detailed view when a user is clicked.

### 3. Data Models & Adapters
- **`Product.kt`**: Data model for products.
- **`ProductAdapter.kt`**: Adapter for products RecyclerView.
- **`UserAdapter.kt`**: Updated with click listener to open `UserDetailFragment`.

## Verification Plan

### Automated Tests
- Build the project (`gradlew assembleDebug`) to ensure all classes, adapters, and XML layouts compile correctly.

### Manual Verification
- Launch app -> See Home Dashboard.
- Tap "Fetch Users" -> Opens Users page with live HTTP data and clickable items leading to details.
- Tap "Fetch Products" -> Opens Products page with live HTTP data from a second endpoint.
- Use back button to return to Home.