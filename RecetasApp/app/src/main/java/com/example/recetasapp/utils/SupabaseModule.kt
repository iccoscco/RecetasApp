package com.example.recetasapp.utils

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseModule {
    val client = createSupabaseClient(
        supabaseUrl = "https://jurljwjmvrtntelllrrl.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp1cmxqd2ptdnJ0bnRlbGxscnJsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjM4NDg0MDUsImV4cCI6MjA3OTQyNDQwNX0.c2nvHVMMhKoaLS8aFYKlnzNBtVpn89ug82yjU6UR31g"
    ) {
        install(Auth)
        install(Postgrest)
    }
}