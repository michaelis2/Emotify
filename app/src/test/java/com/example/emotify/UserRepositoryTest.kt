import android.os.Looper
import android.util.Log
import com.example.emotify.Model.User
import com.example.emotify.Model.UserRepository
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import io.mockk.*
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private lateinit var mockAuth: FirebaseAuth
    private lateinit var mockFirestore: FirebaseFirestore
    private lateinit var mockUser: FirebaseUser
    private lateinit var mockDocumentRef: DocumentReference

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        // Mock static Firebase calls
        mockkStatic(FirebaseApp::class)
        mockkStatic(FirebaseAuth::class)
        mockkStatic(FirebaseFirestore::class)
        mockkStatic(Looper::class)

        every { Looper.getMainLooper() } returns mockk() // Prevent Looper crash

        // Create mock instances
        mockAuth = mockk(relaxed = true)
        mockFirestore = mockk(relaxed = true)
        mockUser = mockk(relaxed = true)
        mockDocumentRef = mockk(relaxed = true)

        every { FirebaseAuth.getInstance() } returns mockAuth
        every { FirebaseFirestore.getInstance() } returns mockFirestore

        // Simulate authenticated user
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns "mockUserId"

        // Simulate Firestore document reference
        every { mockFirestore.collection("userData").document("mockUserId") } returns mockDocumentRef

        // Initialize UserRepository without modifying its constructor
        userRepository = UserRepository()
    }

    @Test
    fun `saveUserData should call onFailure if user is not authenticated`() {
        // Simulate no authenticated user
        every { mockAuth.currentUser } returns null

        val user = User("JohnDoe", 25, "Male")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        // Call the function
        userRepository.saveUserData(user, onSuccess, onFailure)


        // Verify that onFailure was called with the correct exception
        verify { onFailure(match { it.message == "User not authenticated" }) }
        verify(exactly = 0) { onSuccess() }
    }
    @Test
    fun `saveUserData should call onSuccess when Firestore operation succeeds`() {
        val user = User("JohnDoe", 25, "Male")
        val onSuccess: () -> Unit = mockk(relaxed = true)
        val onFailure: (Exception) -> Unit = mockk(relaxed = true)

        val successTask: Task<Void> = mockk(relaxed = true)
        every { mockDocumentRef.set(user) } returns successTask
        every { successTask.addOnSuccessListener(any()) } answers {
            firstArg<OnSuccessListener<Void>>().onSuccess(null)
            successTask
        }

        userRepository.saveUserData(user, onSuccess, onFailure)

        verify { onSuccess() }
        verify(exactly = 0) { onFailure(any()) }
    }

}
