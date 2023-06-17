package com.sample.simpsonsviewer.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sample.simpsonsviewer.log.LogAdapter
import com.sample.simpsonsviewer.serialization.AppSerializer
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import java.io.Serializable

@OptIn(ExperimentalCoroutinesApi::class)
open class DefaultUnitTest {

    protected lateinit var bundle: Bundle
    protected lateinit var context: Context
    protected lateinit var intent: Intent
    protected val bundleKeySlot = slot<String>()
    protected val bundleIntValueSlot = slot<Int>()
    protected val bundleStringValueSlot = slot<String>()
    protected val bundleSerializableValueSlot = slot<Serializable>()
    protected lateinit var logAdapter: LogAdapter
    protected val bundleSlot = slot<Bundle>()
    protected val extrasSlot = slot<Any>()
    protected val intentSlot = slot<Intent>()
    protected val intSlot = slot<Int>()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    val coroutineScope = TestScope()

    protected lateinit var appSerializer: AppSerializer
    protected lateinit var activity: Activity

    open fun setUp() {
//        Dispatchers.setMain(mainThreadSurrogate)
        Dispatchers.setMain(StandardTestDispatcher(coroutineScope.testScheduler))

        mockkConstructor(Bundle::class)
        mockkConstructor(Intent::class)

        logAdapter = mockk(relaxed = true)
        bundle = mockk(relaxed = true)
        context = mockk(relaxed = true)
        intent = mockk(relaxed = true)
        activity = mockk(relaxed = true)

        appSerializer = AppSerializer()

        every {
            anyConstructed<Bundle>().putInt(
                capture(bundleKeySlot),
                capture(bundleIntValueSlot)
            )
        } answers { }
        every {
            anyConstructed<Bundle>().putString(
                capture(bundleKeySlot),
                capture(bundleStringValueSlot)
            )
        } answers { }
        every {
            anyConstructed<Bundle>().putSerializable(
                capture(bundleKeySlot),
                capture(bundleSerializableValueSlot)
            )
        } answers { }
        coEvery {
            anyConstructed<Bundle>().putInt(
                capture(bundleKeySlot),
                capture(bundleIntValueSlot)
            )
        } answers { }
        coEvery {
            anyConstructed<Bundle>().putString(
                capture(bundleKeySlot),
                capture(bundleStringValueSlot)
            )
        } answers { }
        coEvery {
            anyConstructed<Bundle>().putSerializable(
                capture(bundleKeySlot),
                capture(bundleSerializableValueSlot)
            )
        } answers { }
    }

    open fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    protected fun <T> validateEquality(expectation: T, actual: T, message: String = "") {
        Assert.assertEquals(
            "$message; expected=[${expectation}] actual=[$actual]",
            expectation,
            actual
        )
    }

    protected fun <T> validateInEquality(expectation: T, actual: T) {
        Assert.assertNotEquals(
            "expected=[${expectation}] actual=[$actual]",
            expectation,
            actual
        )
    }

    protected fun <T : Serializable> stubGetSerializableFromBundle(
        key: String?,
        value: T?,
        clazz: Class<T>
    ) {
        every {
            bundle.getSerializable(
                key,
                clazz
            )
        } answers {
            return@answers value
        }
        every {
            bundle.getSerializable(key)
        } answers {
            return@answers value
        }
        coEvery {
            bundle.getSerializable(
                key,
                clazz
            )
        } answers {
            return@answers value
        }
        coEvery {
            bundle.getSerializable(key)
        } answers {
            return@answers value
        }
    }

    companion object {
        @JvmStatic
        protected val text = "keywords"
        @JvmStatic
        protected val mockResponse = """
            {
"Abstract": "",
"AbstractSource": "Wikipedia",
"AbstractText": "",
"AbstractURL": "https://en.wikipedia.org/wiki/The_Simpsons_characters",
"Answer": "",
"AnswerType": "",
"Definition": "",
"DefinitionSource": "",
"DefinitionURL": "",
"Entity": "",
"Heading": "The Simpsons characters",
"Image": "",
"ImageHeight": 0,
"ImageIsLogo": 0,
"ImageWidth": 0,
"Infobox": "",
"Redirect": "",
"RelatedTopics": [
{
"FirstURL": "https://duckduckgo.com/Apu_Nahasapeemapetilan",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Apu_Nahasapeemapetilan\">Apu Nahasapeemapetilan</a><br>Apu Nahasapeemapetilan is a recurring character in the American animated television series The Simpsons. He is an Indian immigrant proprietor who runs the Kwik-E-Mart, a popular convenience store in Springfield, and is known for his catchphrase, \"Thank you, come again\".",
"Text": "Apu Nahasapeemapetilan - Apu Nahasapeemapetilan is a recurring character in the American animated television series The Simpsons. He is an Indian immigrant proprietor who runs the Kwik-E-Mart, a popular convenience store in Springfield, and is known for his catchphrase, \"Thank you, come again\"."
},
{
"FirstURL": "https://duckduckgo.com/Apu_Nahasapeemapetilon",
"Icon": {
"Height": "",
"URL": "/i/99b04638.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Apu_Nahasapeemapetilon\">Apu Nahasapeemapetilon</a><br>Apu Nahasapeemapetilon is a recurring character in the American animated television series The Simpsons. He is an Indian immigrant proprietor who runs the Kwik-E-Mart, a popular convenience store in Springfield, and is known for his catchphrase, \"Thank you, come again\".",
"Text": "Apu Nahasapeemapetilon - Apu Nahasapeemapetilon is a recurring character in the American animated television series The Simpsons. He is an Indian immigrant proprietor who runs the Kwik-E-Mart, a popular convenience store in Springfield, and is known for his catchphrase, \"Thank you, come again\"."
},
{
"FirstURL": "https://duckduckgo.com/Barney_Gumble",
"Icon": {
"Height": "",
"URL": "/i/39ce98c0.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Barney_Gumble\">Barney Gumble</a><br>Barnard Arnold \"Barney\" Gumble is a recurring character in the American animated TV series The Simpsons. He is voiced by Dan Castellaneta and first appeared in the series premiere episode \"Simpsons Roasting on an Open Fire\". Barney is the town drunk of Springfield and one of Homer Simpson's friends.",
"Text": "Barney Gumble - Barnard Arnold \"Barney\" Gumble is a recurring character in the American animated TV series The Simpsons. He is voiced by Dan Castellaneta and first appeared in the series premiere episode \"Simpsons Roasting on an Open Fire\". Barney is the town drunk of Springfield and one of Homer Simpson's friends."
},
{
"FirstURL": "https://duckduckgo.com/Bart_Simpson",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Bart_Simpson\">Bart Simpson</a><br>Bartholomew Jojo \"Bart\" Simpson is a fictional character in the American animated television series The Simpsons and part of the Simpson family. He is voiced by Nancy Cartwright and first appeared on television in The Tracey Ullman Show short \"Good Night\" on April 19, 1987.",
"Text": "Bart Simpson - Bartholomew Jojo \"Bart\" Simpson is a fictional character in the American animated television series The Simpsons and part of the Simpson family. He is voiced by Nancy Cartwright and first appeared on television in The Tracey Ullman Show short \"Good Night\" on April 19, 1987."
},
{
"FirstURL": "https://duckduckgo.com/Bender_(Futurama)",
"Icon": {
"Height": "",
"URL": "/i/cb4121fd.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Bender_(Futurama)\">Bender (Futurama)</a><br>Bender Bending Rodríguez is one of the main characters in the animated television series Futurama. He was conceived by the series' creators Matt Groening and David X. Cohen, and is voiced by John DiMaggio.",
"Text": "Bender (Futurama) - Bender Bending Rodríguez is one of the main characters in the animated television series Futurama. He was conceived by the series' creators Matt Groening and David X. Cohen, and is voiced by John DiMaggio."
},
{
"FirstURL": "https://duckduckgo.com/Bleeding_Gums_Murphy",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Bleeding_Gums_Murphy\">Bleeding Gums Murphy</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Bleeding Gums Murphy - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Chief_Wiggum",
"Icon": {
"Height": "",
"URL": "/i/f0eb79ee.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Chief_Wiggum\">Chief Wiggum</a><br>Chief Clancy Wiggum is a fictional character from the animated television series The Simpsons, voiced by Hank Azaria. He is the chief of police in the show's setting of Springfield, and is the father of Ralph Wiggum and the husband of Sarah Wiggum.",
"Text": "Chief Wiggum - Chief Clancy Wiggum is a fictional character from the animated television series The Simpsons, voiced by Hank Azaria. He is the chief of police in the show's setting of Springfield, and is the father of Ralph Wiggum and the husband of Sarah Wiggum."
},
{
"FirstURL": "https://duckduckgo.com/Cletus_Spuckler",
"Icon": {
"Height": "",
"URL": "/i/4d663a85.gif",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Cletus_Spuckler\">Cletus Spuckler</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Cletus Spuckler - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Comic_Book_Guy",
"Icon": {
"Height": "",
"URL": "/i/911ab519.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Comic_Book_Guy\">Comic Book Guy</a><br>Comic Book Guy is the common, popular name for Jeffrey \"Jeff\" Albertson, a recurring fictional character in the animated television series The Simpsons. He is voiced by Hank Azaria and first appeared in the second-season episode \"Three Men and a Comic Book\", which originally aired on May 9, 1991.",
"Text": "Comic Book Guy - Comic Book Guy is the common, popular name for Jeffrey \"Jeff\" Albertson, a recurring fictional character in the animated television series The Simpsons. He is voiced by Hank Azaria and first appeared in the second-season episode \"Three Men and a Comic Book\", which originally aired on May 9, 1991."
},
{
"FirstURL": "https://duckduckgo.com/Declan_Desmond",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Declan_Desmond\">Declan Desmond</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Declan Desmond - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Disco_Stu",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Disco_Stu\">Disco Stu</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Disco Stu - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Drederick_Tatum",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Drederick_Tatum\">Drederick Tatum</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Drederick Tatum - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Dr._Hibbert",
"Icon": {
"Height": "",
"URL": "/i/c9497b7e.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Dr._Hibbert\">Dr. Hibbert</a><br>Dr. Julius Michael Hibbert, M.D. is a recurring character on the television animated sitcom The Simpsons. He is Springfield's most prominent medical professional. Although he has a kind and warm persona, he is also often characterized as greedy and lacking in empathy.",
"Text": "Dr. Hibbert - Dr. Julius Michael Hibbert, M.D. is a recurring character on the television animated sitcom The Simpsons. He is Springfield's most prominent medical professional. Although he has a kind and warm persona, he is also often characterized as greedy and lacking in empathy."
},
{
"FirstURL": "https://duckduckgo.com/Dr._Nick",
"Icon": {
"Height": "",
"URL": "/i/db757c91.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Dr._Nick\">Dr. Nick</a><br>Dr. Nicholas Riviera is a recurring fictional character in the American animated sitcom The Simpsons. He is voiced by Hank Azaria and first appeared in the episode \"Bart Gets Hit by a Car\". Dr. Nick is an inept quack physician, and a satire of incompetent medical professionals.",
"Text": "Dr. Nick - Dr. Nicholas Riviera is a recurring fictional character in the American animated sitcom The Simpsons. He is voiced by Hank Azaria and first appeared in the episode \"Bart Gets Hit by a Car\". Dr. Nick is an inept quack physician, and a satire of incompetent medical professionals."
},
{
"FirstURL": "https://duckduckgo.com/Dr._Velimirovic",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Dr._Velimirovic\">Dr. Velimirovic</a><br>\"Pygmoelian\" is the sixteenth episode of the eleventh season of the American animated sitcom The Simpsons. It originally aired on the Fox network in the United States on February 27, 2000.",
"Text": "Dr. Velimirovic - \"Pygmoelian\" is the sixteenth episode of the eleventh season of the American animated sitcom The Simpsons. It originally aired on the Fox network in the United States on February 27, 2000."
},
{
"FirstURL": "https://duckduckgo.com/Duffman",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Duffman\">Duffman</a><br>Barry Huffman, more commonly known by his work name Duffman, is a character on the animated sitcom The Simpsons. He is the mascot and chief spokesperson for Duff Beer.",
"Text": "Duffman - Barry Huffman, more commonly known by his work name Duffman, is a character on the animated sitcom The Simpsons. He is the mascot and chief spokesperson for Duff Beer."
},
{
"FirstURL": "https://duckduckgo.com/Edna_Krabappel",
"Icon": {
"Height": "",
"URL": "/i/1bdfff5e.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Edna_Krabappel\">Edna Krabappel</a><br>Edna Krabappel-Flanders is a fictional character from the American animated sitcom The Simpsons, voiced by Marcia Wallace from 1990 until her death in October 2013. She was a 4th-grade teacher, who taught Bart Simpson's class at Springfield Elementary School.",
"Text": "Edna Krabappel - Edna Krabappel-Flanders is a fictional character from the American animated sitcom The Simpsons, voiced by Marcia Wallace from 1990 until her death in October 2013. She was a 4th-grade teacher, who taught Bart Simpson's class at Springfield Elementary School."
},
{
"FirstURL": "https://duckduckgo.com/Fat_Tony_(The_Simpsons)",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Fat_Tony_(The_Simpsons)\">Fat Tony (The Simpsons)</a><br>Anthony \"Fat Tony\" D'Amico is the name of two recurring characters in the animated sitcom The Simpsons. Both are voiced by Joe Mantegna and first appeared in the episode \"Bart the Murderer\" of the third season. Fat Tony is a mobster and the underboss of the Springfield Mafia.",
"Text": "Fat Tony (The Simpsons) - Anthony \"Fat Tony\" D'Amico is the name of two recurring characters in the animated sitcom The Simpsons. Both are voiced by Joe Mantegna and first appeared in the episode \"Bart the Murderer\" of the third season. Fat Tony is a mobster and the underboss of the Springfield Mafia."
},
{
"FirstURL": "https://duckduckgo.com/Graggle_Simpson",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Graggle_Simpson\">Graggle Simpson</a><br>Gumbly Simpson or Graggle is a fictional character purported to be from the American animated sitcom The Simpsons.",
"Text": "Graggle Simpson - Gumbly Simpson or Graggle is a fictional character purported to be from the American animated sitcom The Simpsons."
},
{
"FirstURL": "https://duckduckgo.com/Grampa_Simpson",
"Icon": {
"Height": "",
"URL": "/i/7b1c968b.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Grampa_Simpson\">Grampa Simpson</a><br>Abraham Jebediah \"Abe\" Simpson II, better known as Grampa, is a recurring character in the animated television series The Simpsons.",
"Text": "Grampa Simpson - Abraham Jebediah \"Abe\" Simpson II, better known as Grampa, is a recurring character in the animated television series The Simpsons."
},
{
"FirstURL": "https://duckduckgo.com/Groundskeeper_Willie",
"Icon": {
"Height": "",
"URL": "/i/f845601f.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Groundskeeper_Willie\">Groundskeeper Willie</a><br>William MacMoran MacDougal, better known as Groundskeeper Willie, is a recurring character on The Simpsons, voiced by Dan Castellaneta. He is the head groundskeeper and Janitor at Springfield Elementary School. Willie is almost feral in nature and is immensely proud of his Scottish origin.",
"Text": "Groundskeeper Willie - William MacMoran MacDougal, better known as Groundskeeper Willie, is a recurring character on The Simpsons, voiced by Dan Castellaneta. He is the head groundskeeper and Janitor at Springfield Elementary School. Willie is almost feral in nature and is immensely proud of his Scottish origin."
},
{
"FirstURL": "https://duckduckgo.com/Hans_Moleman",
"Icon": {
"Height": "",
"URL": "/i/9f651728.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Hans_Moleman\">Hans Moleman</a><br>Hans Moleman is a recurring character on the animated television series The Simpsons. He was created by series creator Matt Groening and is voiced by Dan Castellaneta and first appeared in the episode \"Principal Charming\".",
"Text": "Hans Moleman - Hans Moleman is a recurring character on the animated television series The Simpsons. He was created by series creator Matt Groening and is voiced by Dan Castellaneta and first appeared in the episode \"Principal Charming\"."
},
{
"FirstURL": "https://duckduckgo.com/Homer_Simpson",
"Icon": {
"Height": "",
"URL": "/i/bf0eb228.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Homer_Simpson\">Homer Simpson</a><br>Homer Jay Simpson is one of the main characters of the American animated sitcom The Simpsons. He is voiced by Dan Castellaneta and first appeared, along with the rest of his family, in The Tracey Ullman Show short \"Good Night\" on April 19, 1987.",
"Text": "Homer Simpson - Homer Jay Simpson is one of the main characters of the American animated sitcom The Simpsons. He is voiced by Dan Castellaneta and first appeared, along with the rest of his family, in The Tracey Ullman Show short \"Good Night\" on April 19, 1987."
},
{
"FirstURL": "https://duckduckgo.com/Itchy_(The_Simpsons)",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Itchy_(The_Simpsons)\">Itchy (The Simpsons)</a><br>The Itchy & Scratchy Show is a fictional animated television series featured in the American animated television series The Simpsons. It appears as a part of The Krusty the Clown Show.",
"Text": "Itchy (The Simpsons) - The Itchy & Scratchy Show is a fictional animated television series featured in the American animated television series The Simpsons. It appears as a part of The Krusty the Clown Show."
},
{
"FirstURL": "https://duckduckgo.com/Jimbo_Jones",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Jimbo_Jones\">Jimbo Jones</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Jimbo Jones - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Judge_Constance_Harm",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Judge_Constance_Harm\">Judge Constance Harm</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Judge Constance Harm - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Kang_and_Kodos",
"Icon": {
"Height": "",
"URL": "/i/be84f45b.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Kang_and_Kodos\">Kang and Kodos</a><br>Kang and Kodos Johnson are a duo of fictional recurring characters in the animated television series The Simpsons. Kang is voiced by Harry Shearer and Kodos by Dan Castellaneta.",
"Text": "Kang and Kodos - Kang and Kodos Johnson are a duo of fictional recurring characters in the animated television series The Simpsons. Kang is voiced by Harry Shearer and Kodos by Dan Castellaneta."
},
{
"FirstURL": "https://duckduckgo.com/Kearney_Zzyzwicz",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Kearney_Zzyzwicz\">Kearney Zzyzwicz</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Kearney Zzyzwicz - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Kent_Brockman",
"Icon": {
"Height": "",
"URL": "/i/b6c36d00.jpg",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Kent_Brockman\">Kent Brockman</a><br>Kent Brockman is a fictional character in the animated television series The Simpsons. He is voiced by Harry Shearer and first appeared in the episode \"Krusty Gets Busted\". He is a grumpy, self-centered local Springfield news anchor.",
"Text": "Kent Brockman - Kent Brockman is a fictional character in the animated television series The Simpsons. He is voiced by Harry Shearer and first appeared in the episode \"Krusty Gets Busted\". He is a grumpy, self-centered local Springfield news anchor."
},
{
"FirstURL": "https://duckduckgo.com/Krusty_the_Clown",
"Icon": {
"Height": "",
"URL": "/i/caa07f01.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Krusty_the_Clown\">Krusty the Clown</a><br>Herschel Shmoikel Pinchas Yerucham Krustofsky, better known by his stage name Krusty the Clown, is a recurring character on the animated television series The Simpsons. He is voiced by Dan Castellaneta.",
"Text": "Krusty the Clown - Herschel Shmoikel Pinchas Yerucham Krustofsky, better known by his stage name Krusty the Clown, is a recurring character on the animated television series The Simpsons. He is voiced by Dan Castellaneta."
},
{
"FirstURL": "https://duckduckgo.com/Lenny_and_Carl",
"Icon": {
"Height": "",
"URL": "/i/cde0d56a.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Lenny_and_Carl\">Lenny and Carl</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Lenny and Carl - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Lionel_Hutz",
"Icon": {
"Height": "",
"URL": "/i/94d72e52.jpg",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Lionel_Hutz\">Lionel Hutz</a><br>Lionel Hutz is a fictional character in the American animated sitcom The Simpsons. He was voiced by Phil Hartman, and his first appearance was in the season two episode \"Bart Gets Hit by a Car\".",
"Text": "Lionel Hutz - Lionel Hutz is a fictional character in the American animated sitcom The Simpsons. He was voiced by Phil Hartman, and his first appearance was in the season two episode \"Bart Gets Hit by a Car\"."
},
{
"FirstURL": "https://duckduckgo.com/Lisa_Simpson",
"Icon": {
"Height": "",
"URL": "/i/0c6d3b53.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Lisa_Simpson\">Lisa Simpson</a><br>Lisa Marie Simpson is a fictional character in the animated television series The Simpsons. She is the middle child and most accomplished of the Simpson family. Voiced by Yeardley Smith, Lisa was born as a character in The Tracey Ullman Show short \"Good Night\" on April 19, 1987.",
"Text": "Lisa Simpson - Lisa Marie Simpson is a fictional character in the animated television series The Simpsons. She is the middle child and most accomplished of the Simpson family. Voiced by Yeardley Smith, Lisa was born as a character in The Tracey Ullman Show short \"Good Night\" on April 19, 1987."
},
{
"FirstURL": "https://duckduckgo.com/Maggie_Simpson",
"Icon": {
"Height": "",
"URL": "/i/c018dead.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Maggie_Simpson\">Maggie Simpson</a><br>Margaret Evelyn Lenny \"Maggie\" Simpson is a fictional character in the animated television series The Simpsons and a part of the Simpson family, notably the youngest member. She first appeared on television in the Tracey Ullman Show short \"Good Night\" on April 19, 1987.",
"Text": "Maggie Simpson - Margaret Evelyn Lenny \"Maggie\" Simpson is a fictional character in the animated television series The Simpsons and a part of the Simpson family, notably the youngest member. She first appeared on television in the Tracey Ullman Show short \"Good Night\" on April 19, 1987."
},
{
"FirstURL": "https://duckduckgo.com/Marge_Simpson",
"Icon": {
"Height": "",
"URL": "/i/b75bd67d.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Marge_Simpson\">Marge Simpson</a><br>Marjorie Jacqueline \"Marge\" Simpson is a character in the American animated sitcom The Simpsons and part of the eponymous family. Voiced by Julie Kavner, she first appeared on television in The Tracey Ullman Show short \"Good Night\" on April 19, 1987.",
"Text": "Marge Simpson - Marjorie Jacqueline \"Marge\" Simpson is a character in the American animated sitcom The Simpsons and part of the eponymous family. Voiced by Julie Kavner, she first appeared on television in The Tracey Ullman Show short \"Good Night\" on April 19, 1987."
},
{
"FirstURL": "https://duckduckgo.com/Mayor_Quimby",
"Icon": {
"Height": "",
"URL": "/i/93d80e95.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Mayor_Quimby\">Mayor Quimby</a><br>Mayor Joseph Fitzgerald O'Malley Fitzpatrick O'Donnell The Edge \"Joe\" Quimby, nicknamed Diamond Joe, is a recurring character from the animated sitcom television series The Simpsons. He is voiced by Dan Castellaneta, and first appeared in the episode \"Bart Gets an 'F'\".",
"Text": "Mayor Quimby - Mayor Joseph Fitzgerald O'Malley Fitzpatrick O'Donnell The Edge \"Joe\" Quimby, nicknamed Diamond Joe, is a recurring character from the animated sitcom television series The Simpsons. He is voiced by Dan Castellaneta, and first appeared in the episode \"Bart Gets an 'F'\"."
},
{
"FirstURL": "https://duckduckgo.com/Milhouse_Van_Houten",
"Icon": {
"Height": "",
"URL": "/i/c1923cb1.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Milhouse_Van_Houten\">Milhouse Van Houten</a><br>Milhouse Mussolini Van Houten is a recurring character in the Fox animated television series The Simpsons voiced by Pamela Hayden and created by Matt Groening. Milhouse is Bart Simpson's best friend in Mrs. Krabappel's fourth grade class at Springfield Elementary School.",
"Text": "Milhouse Van Houten - Milhouse Mussolini Van Houten is a recurring character in the Fox animated television series The Simpsons voiced by Pamela Hayden and created by Matt Groening. Milhouse is Bart Simpson's best friend in Mrs. Krabappel's fourth grade class at Springfield Elementary School."
},
{
"FirstURL": "https://duckduckgo.com/Moe_Szyslak",
"Icon": {
"Height": "",
"URL": "/i/388580ad.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Moe_Szyslak\">Moe Szyslak</a><br>Moe Szyslak is a recurring character from the animated television series The Simpsons. He is voiced by Hank Azaria and first appeared in the series premiere episode \"Simpsons Roasting on an Open Fire\".",
"Text": "Moe Szyslak - Moe Szyslak is a recurring character from the animated television series The Simpsons. He is voiced by Hank Azaria and first appeared in the series premiere episode \"Simpsons Roasting on an Open Fire\"."
},
{
"FirstURL": "https://duckduckgo.com/Mona_Simpson_(The_Simpsons)",
"Icon": {
"Height": "",
"URL": "/i/d6efc652.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Mona_Simpson_(The_Simpsons)\">Mona Simpson (The Simpsons)</a><br>Mona Penelope Simpson is a fictional guest character in the animated television series The Simpsons. She is voiced most prominently by Glenn Close, but has also been voiced by Maggie Roswell, Tress MacNeille, and Pamela Hayden.",
"Text": "Mona Simpson (The Simpsons) - Mona Penelope Simpson is a fictional guest character in the animated television series The Simpsons. She is voiced most prominently by Glenn Close, but has also been voiced by Maggie Roswell, Tress MacNeille, and Pamela Hayden."
},
{
"FirstURL": "https://duckduckgo.com/Mr._Burns",
"Icon": {
"Height": "",
"URL": "/i/cf311517.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Mr._Burns\">Mr. Burns</a><br>Charles Montgomery Plantagenet Schicklgruber \"Monty\" Burns, usually referred to as Mr. Burns, Monty, or C. Montgomery Burns, is a recurring character and the main antagonist of the animated television series The Simpsons, voiced initially by Christopher Collins and currently by Harry Shearer.",
"Text": "Mr. Burns - Charles Montgomery Plantagenet Schicklgruber \"Monty\" Burns, usually referred to as Mr. Burns, Monty, or C. Montgomery Burns, is a recurring character and the main antagonist of the animated television series The Simpsons, voiced initially by Christopher Collins and currently by Harry Shearer."
},
{
"FirstURL": "https://duckduckgo.com/Nahasapeemapetilon_octuplets",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Nahasapeemapetilon_octuplets\">Nahasapeemapetilon octuplets</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Nahasapeemapetilon octuplets - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Ned_Flanders",
"Icon": {
"Height": "",
"URL": "/i/ad454204.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Ned_Flanders\">Ned Flanders</a><br>Nedward \"Ned\" Flanders Jr. is a fictional character in the animated television series The Simpsons, voiced by Harry Shearer and first appearing in the series premiere episode \"Simpsons Roasting on an Open Fire.\"",
"Text": "Ned Flanders - Nedward \"Ned\" Flanders Jr. is a fictional character in the animated television series The Simpsons, voiced by Harry Shearer and first appearing in the series premiere episode \"Simpsons Roasting on an Open Fire.\""
},
{
"FirstURL": "https://duckduckgo.com/Nelson_Muntz",
"Icon": {
"Height": "",
"URL": "/i/23702790.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Nelson_Muntz\">Nelson Muntz</a><br>Nelson Mandela Muntz is a fictional character and the lead school bully from the animated television series The Simpsons, where he is best known for his signature mocking laugh \"Ha-ha! \". He is voiced by Nancy Cartwright.",
"Text": "Nelson Muntz - Nelson Mandela Muntz is a fictional character and the lead school bully from the animated television series The Simpsons, where he is best known for his signature mocking laugh \"Ha-ha! \". He is voiced by Nancy Cartwright."
},
{
"FirstURL": "https://duckduckgo.com/Otto_Mann",
"Icon": {
"Height": "",
"URL": "/i/9efe5c40.gif",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Otto_Mann\">Otto Mann</a><br>Otto Mann is a fictional character on the American animated TV series The Simpsons, voiced by Harry Shearer. He is the school bus driver for Springfield Elementary School, and is known or implied to be a user of several drugs.",
"Text": "Otto Mann - Otto Mann is a fictional character on the American animated TV series The Simpsons, voiced by Harry Shearer. He is the school bus driver for Springfield Elementary School, and is known or implied to be a user of several drugs."
},
{
"FirstURL": "https://duckduckgo.com/Patty_and_Selma",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Patty_and_Selma\">Patty and Selma</a><br>Patricia Maleficent \"Patty\" Bouvier and Selma Bouvier-Terwilliger-Hutz-McClure-Discotheque-Simpson-D'Amico are fictional characters in the American animated sitcom The Simpsons. They are identical twins and are voiced by Julie Kavner who also voices their sister Marge.",
"Text": "Patty and Selma - Patricia Maleficent \"Patty\" Bouvier and Selma Bouvier-Terwilliger-Hutz-McClure-Discotheque-Simpson-D'Amico are fictional characters in the American animated sitcom The Simpsons. They are identical twins and are voiced by Julie Kavner who also voices their sister Marge."
},
{
"FirstURL": "https://duckduckgo.com/Plopper",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Plopper\">Plopper</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Plopper - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Principal_Skinner",
"Icon": {
"Height": "",
"URL": "/i/785a66ec.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Principal_Skinner\">Principal Skinner</a><br>Principal Seymour Skinner is a recurring fictional character in the animated sitcom The Simpsons, who is voiced by Harry Shearer.",
"Text": "Principal Skinner - Principal Seymour Skinner is a recurring fictional character in the animated sitcom The Simpsons, who is voiced by Harry Shearer."
},
{
"FirstURL": "https://duckduckgo.com/Professor_Frink",
"Icon": {
"Height": "",
"URL": "/i/c9dc0e13.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Professor_Frink\">Professor Frink</a><br>Professor John I.Q. Nerdelbaum Frink Jr., is a recurring character in the animated television series The Simpsons. He is voiced by Hank Azaria, and first appeared in the 1991 episode \"Old Money\".",
"Text": "Professor Frink - Professor John I.Q. Nerdelbaum Frink Jr., is a recurring character in the animated television series The Simpsons. He is voiced by Hank Azaria, and first appeared in the 1991 episode \"Old Money\"."
},
{
"FirstURL": "https://duckduckgo.com/Radioactive_Man_(The_Simpsons_character)",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Radioactive_Man_(The_Simpsons_character)\">Radioactive Man (The Simpsons character)</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Radioactive Man (The Simpsons character) - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Ralph_Wiggum",
"Icon": {
"Height": "",
"URL": "/i/2f9c97eb.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Ralph_Wiggum\">Ralph Wiggum</a><br>Ralph Wiggum is a recurring character on the animated series, The Simpsons. He is voiced by Nancy Cartwright. Ralph, the supposed son of Police Chief Wiggum, is a classmate of Lisa Simpson and is characterized by his frequent non-sequiturs and humorous behavior.",
"Text": "Ralph Wiggum - Ralph Wiggum is a recurring character on the animated series, The Simpsons. He is voiced by Nancy Cartwright. Ralph, the supposed son of Police Chief Wiggum, is a classmate of Lisa Simpson and is characterized by his frequent non-sequiturs and humorous behavior."
},
{
"FirstURL": "https://duckduckgo.com/Reverend_Lovejoy",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Reverend_Lovejoy\">Reverend Lovejoy</a><br>Reverend Timothy \"Tim\" Lovejoy, Jr. is a recurring character in the animated television series The Simpsons. He is voiced by Harry Shearer, and first appeared in the episode \"The Telltale Head\". Rev. Lovejoy is the minister at The First Church of Springfield—the Protestant church in Springfield.",
"Text": "Reverend Lovejoy - Reverend Timothy \"Tim\" Lovejoy, Jr. is a recurring character in the animated television series The Simpsons. He is voiced by Harry Shearer, and first appeared in the episode \"The Telltale Head\". Rev. Lovejoy is the minister at The First Church of Springfield—the Protestant church in Springfield."
},
{
"FirstURL": "https://duckduckgo.com/Roger_Meyers%2C_Sr.",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Roger_Meyers%2C_Sr.\">Roger Meyers, Sr.</a><br>The Itchy & Scratchy Show is a fictional animated television series featured in the American animated television series The Simpsons. It appears as a part of The Krusty the Clown Show.",
"Text": "Roger Meyers, Sr. - The Itchy & Scratchy Show is a fictional animated television series featured in the American animated television series The Simpsons. It appears as a part of The Krusty the Clown Show."
},
{
"FirstURL": "https://duckduckgo.com/Santa's_Little_Helper",
"Icon": {
"Height": "",
"URL": "/i/6825f3f5.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Santa's_Little_Helper\">Santa's Little Helper</a><br>Santa's Little Helper is a fictional dog in the American animated television series The Simpsons. He is the pet greyhound of the Simpson family. He was previously voiced by Frank Welker, and is currently voiced by Dan Castellaneta.",
"Text": "Santa's Little Helper - Santa's Little Helper is a fictional dog in the American animated television series The Simpsons. He is the pet greyhound of the Simpson family. He was previously voiced by Frank Welker, and is currently voiced by Dan Castellaneta."
},
{
"FirstURL": "https://duckduckgo.com/Sideshow_Bob",
"Icon": {
"Height": "",
"URL": "/i/922b6a1c.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Sideshow_Bob\">Sideshow Bob</a><br>Robert Underunk Terwilliger Jr., PhD, better known for being Sideshow Bob, is a recurring character in the animated television series The Simpsons. He is voiced by Kelsey Grammer and first appeared in the episode \"The Telltale Head\".",
"Text": "Sideshow Bob - Robert Underunk Terwilliger Jr., PhD, better known for being Sideshow Bob, is a recurring character in the animated television series The Simpsons. He is voiced by Kelsey Grammer and first appeared in the episode \"The Telltale Head\"."
},
{
"FirstURL": "https://duckduckgo.com/Sideshow_Mel",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Sideshow_Mel\">Sideshow Mel</a><br>Sideshow Mel, full name Melvin Van Horne, is a character on the animated TV show The Simpsons. Sideshow Mel has served as Krusty the Clown's sidekick for more than a decade, replacing Sideshow Bob after Bob was incarcerated from framing Krusty for robbery.",
"Text": "Sideshow Mel - Sideshow Mel, full name Melvin Van Horne, is a character on the animated TV show The Simpsons. Sideshow Mel has served as Krusty the Clown's sidekick for more than a decade, replacing Sideshow Bob after Bob was incarcerated from framing Krusty for robbery."
},
{
"FirstURL": "https://duckduckgo.com/Simpson_family",
"Icon": {
"Height": "",
"URL": "/i/d58b5fe4.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Simpson_family\">Simpson family</a><br>The Simpson family are the fictional characters featured in the animated television series The Simpsons. The Simpsons are a nuclear family consisting of married couple Homer and Marge and their three children, Bart, Lisa, and Maggie.",
"Text": "Simpson family - The Simpson family are the fictional characters featured in the animated television series The Simpsons. The Simpsons are a nuclear family consisting of married couple Homer and Marge and their three children, Bart, Lisa, and Maggie."
},
{
"FirstURL": "https://duckduckgo.com/Snake_Jailbird",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Snake_Jailbird\">Snake Jailbird</a><br>The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals.",
"Text": "Snake Jailbird - The Simpsons includes a large array of supporting/minor characters: co-workers, teachers, family friends, extended relatives, townspeople, local celebrities, fictional characters within the show, and even animals."
},
{
"FirstURL": "https://duckduckgo.com/Snowball_(The_Simpsons)",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Snowball_(The_Simpsons)\">Snowball (The Simpsons)</a><br>The Simpson family consists of fictional characters featured in the animated television series The Simpsons. The Simpsons are a nuclear family consisting of married couple Homer and Marge and their three children Bart, Lisa, and Maggie.",
"Text": "Snowball (The Simpsons) - The Simpson family consists of fictional characters featured in the animated television series The Simpsons. The Simpsons are a nuclear family consisting of married couple Homer and Marge and their three children Bart, Lisa, and Maggie."
},
{
"FirstURL": "https://duckduckgo.com/The_Itchy_%26_Scratchy_Show",
"Icon": {
"Height": "",
"URL": "/i/bc74928e.jpg",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/The_Itchy_%26_Scratchy_Show\">The Itchy & Scratchy Show</a><br>The Itchy & Scratchy Show is a show within a show featured on The Simpsons.",
"Text": "The Itchy & Scratchy Show - The Itchy & Scratchy Show is a show within a show featured on The Simpsons."
},
{
"FirstURL": "https://duckduckgo.com/Troy_McClure",
"Icon": {
"Height": "",
"URL": "/i/20433a15.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Troy_McClure\">Troy McClure</a><br>Troy McClure is a fictional character in the American animated series The Simpsons. He was originally voiced by Phil Hartman and first appeared in the second season episode \"Homer vs. Lisa and the 8th Commandment.\"",
"Text": "Troy McClure - Troy McClure is a fictional character in the American animated series The Simpsons. He was originally voiced by Phil Hartman and first appeared in the second season episode \"Homer vs. Lisa and the 8th Commandment.\""
},
{
"FirstURL": "https://duckduckgo.com/Waylon_Smithers",
"Icon": {
"Height": "",
"URL": "/i/12f25a94.png",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Waylon_Smithers\">Waylon Smithers</a><br>Waylon Joseph Smithers Jr., usually referred to as Mr. Smithers or simply Smithers, is a recurring fictional character in the animated sitcom The Simpsons, voiced by Harry Shearer.",
"Text": "Waylon Smithers - Waylon Joseph Smithers Jr., usually referred to as Mr. Smithers or simply Smithers, is a recurring fictional character in the animated sitcom The Simpsons, voiced by Harry Shearer."
},
{
"FirstURL": "https://duckduckgo.com/Wendy_Sage",
"Icon": {
"Height": "",
"URL": "",
"Width": ""
},
"Result": "<a href=\"https://duckduckgo.com/Wendy_Sage\">Wendy Sage</a><br>Dr. Wendy Sage is a fictional character on the television animated sitcom The Simpsons. She works as a hypnotherapist and she is also a breast cancer survivor. She visibly has one of her breasts removed.",
"Text": "Wendy Sage - Dr. Wendy Sage is a fictional character on the television animated sitcom The Simpsons. She works as a hypnotherapist and she is also a breast cancer survivor. She visibly has one of her breasts removed."
}
],
"Results": [],
"Type": "C",
"meta": {
"attribution": null,
"blockgroup": null,
"created_date": null,
"description": "Wikipedia",
"designer": null,
"dev_date": null,
"dev_milestone": "live",
"developer": [
{
"name": "DDG Team",
"type": "ddg",
"url": "http://www.duckduckhack.com"
}
],
"example_query": "nikola tesla",
"id": "wikipedia_fathead",
"is_stackexchange": null,
"js_callback_name": "wikipedia",
"live_date": null,
"maintainer": {
"github": "duckduckgo"
},
"name": "Wikipedia",
"perl_module": "DDG::Fathead::Wikipedia",
"producer": null,
"production_state": "online",
"repo": "fathead",
"signal_from": "wikipedia_fathead",
"src_domain": "en.wikipedia.org",
"src_id": 1,
"src_name": "Wikipedia",
"src_options": {
"directory": "",
"is_fanon": 0,
"is_mediawiki": 1,
"is_wikipedia": 1,
"language": "en",
"min_abstract_length": "20",
"skip_abstract": 0,
"skip_abstract_paren": 0,
"skip_end": "0",
"skip_icon": 0,
"skip_image_name": 0,
"skip_qr": "",
"source_skip": "",
"src_info": ""
},
"src_url": null,
"status": "live",
"tab": "About",
"topic": [
"productivity"
],
"unsafe": 0
}
}
        """
    }
}
